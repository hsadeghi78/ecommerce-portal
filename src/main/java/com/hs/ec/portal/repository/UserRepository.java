package com.hs.ec.portal.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.hs.ec.portal.domain.Authority;
import com.hs.ec.portal.domain.User;
import com.hs.ec.portal.repository.rowmapper.PartyRowMapper;
import com.hs.ec.portal.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long>, UserRepositoryInternal {
    Mono<User> findOneByActivationKey(String activationKey);

    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    Mono<User> findOneByResetKey(String resetKey);

    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findOneByLogin(String login);

    Flux<User> findAllByIdNotNull(Pageable pageable);

    Flux<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Mono<Long> count();

    @Query("INSERT INTO jhi_user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(Long userId, String authority);

    @Query("DELETE FROM jhi_user_authority")
    Mono<Void> deleteAllUserAuthorities();

    @Query("DELETE FROM jhi_user_authority WHERE user_id = :userId")
    Mono<Void> deleteUserAuthorities(Long userId);
}

interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}

interface UserRepositoryInternal extends DeleteExtended<User> {
    Mono<User> findOneWithAuthoritiesByLogin(String login);

    Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<User> findAllWithAuthorities(Pageable pageable);

    Mono<User> findOneWithEagerRelationships(String login);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final R2dbcConverter r2dbcConverter;

    private final EntityManager entityManager;
    private final UserRowMapper userMapper;
    private final PartyRowMapper partyMapper;
    private static final Table entityTable = Table.aliased("jhi_user", "jhi_user");
    private static final Table partyTable = Table.aliased("party", "e_party");

    public UserRepositoryInternalImpl(
        DatabaseClient db,
        R2dbcEntityTemplate r2dbcEntityTemplate,
        R2dbcConverter r2dbcConverter,
        EntityManager entityManager,
        UserRowMapper userMapper,
        PartyRowMapper partyMapper
    ) {
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.r2dbcConverter = r2dbcConverter;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.partyMapper = partyMapper;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email.toLowerCase());
    }

    @Override
    public Mono<User> findOneWithEagerRelationships(String login) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("login"), Conditions.just("'" + login + "'"));
        return createQuery(null, whereClause)
            .one()
            .flatMap(user -> {
                return findOneWithAuthoritiesByLogin(login)
                    .flatMap(user1 -> {
                        user.setAuthorities(user1.getAuthorities());
                        return Mono.just(user);
                    });
            });
    }

    @Override
    public Flux<User> findAllWithAuthorities(Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("id");
        String direction = String.valueOf(
            pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.DEFAULT_DIRECTION)
        );
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        return db
            .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id")
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("authority_name", String.class)))
            )
            .all()
            .groupBy(t -> t.getT1().getLogin())
            .flatMap(l -> l.collectList().map(t -> updateUserWithAuthorities(t.get(0).getT1(), t)))
            .sort(
                Sort.Direction.fromString(direction) == Sort.DEFAULT_DIRECTION
                    ? new BeanComparator<>(property)
                    : new BeanComparator<>(property).reversed()
            )
            .skip(page * size)
            .take(size);
    }

    @Override
    public Mono<Void> delete(User user) {
        return db
            .sql("DELETE FROM jhi_user_authority WHERE user_id = :userId")
            .bind("userId", user.getId())
            .then()
            .then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(user.getId()))).all().then());
    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db
            .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id WHERE u." + fieldName + " = :" + fieldName)
            .bind(fieldName, fieldValue)
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("authority_name", String.class)))
            )
            .all()
            .collectList()
            .filter(l -> !l.isEmpty())
            .map(l -> updateUserWithAuthorities(l.get(0).getT1(), l));
    }

    private User updateUserWithAuthorities(User user, List<Tuple2<User, Optional<String>>> tuples) {
        user.setAuthorities(
            tuples
                .stream()
                .filter(t -> t.getT2().isPresent())
                .map(t -> {
                    Authority authority = new Authority();
                    authority.setName(t.getT2().orElseThrow());
                    return authority;
                })
                .collect(Collectors.toSet())
        );

        return user;
    }

    RowsFetchSpec<User> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PartySqlHelper.getColumns(partyTable, "party"));
        //columns.addAll(UserAuthoritySqlHelper.getColumns(userAuthorityTable, "user_auth"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(partyTable)
            .on(Column.create("party_id", entityTable))
            .equals(Column.create("id", partyTable));
        //.leftOuterJoin(userAuthorityTable)
        //.on(Column.create("id", entityTable))
        //.equals(Column.create("user_id", userAuthorityTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, User.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    private User process(Row row, RowMetadata metadata) {
        User entity = userMapper.apply(row, "e");
        entity.setParty(partyMapper.apply(row, "party"));
        //entity.setParty(partyMapper.apply(row, "user_authority"));
        return entity;
    }
}
