package edu.oregonstate.cs467.travelplanner.util.security;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "file:sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class AuthenticatedUserProviderTests extends AbstractBaseTest {
    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    @Test
    @WithUserDetails("user1")
    void as_user() {
        assertThat(authUserProvider.isAnyUser()).isTrue();
        assertThat(authUserProvider.isUserWithId(1)).isTrue();
        assertThat(authUserProvider.isUserWithId(2)).isFalse();
    }

    @Test
    @WithAnonymousUser
    void as_anon() {
        assertThat(authUserProvider.isAnyUser()).isFalse();
        assertThat(authUserProvider.isUserWithId(1)).isFalse();
    }
}