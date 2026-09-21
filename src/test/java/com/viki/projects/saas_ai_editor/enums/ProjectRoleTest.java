package com.viki.projects.saas_ai_editor.enums;

// These two imports are the whole toolkit for a simple test:
//   @Test        -> marks a method as a test the runner should execute
//   assertThat   -> AssertJ, the fluent "check the result" library
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Your first test class.
 *
 * MENTAL MODEL — every test follows Arrange -> Act -> Assert:
 *   Arrange: set up the thing you're testing
 *   Act:     do the thing
 *   Assert:  check the result is what you expected
 *
 * Convention: the test class for `ProjectRole` is called `ProjectRoleTest`,
 * and it lives in the SAME package but under src/test instead of src/main.
 */
class ProjectRoleTest {

    // A test method is just a normal method annotated with @Test.
    // Name it as a sentence describing the rule you're proving. Read it out loud:
    // "viewer can view project".
    @Test
    void viewer_canViewProject() {
        // Arrange: grab the role we want to check
        ProjectRole viewer = ProjectRole.VIEWER;

        // Act: ask it for its permissions
        var permissions = viewer.getPermissions();

        // Assert: it SHOULD contain VIEW
        assertThat(permissions).contains(ProjectPermission.VIEW);
    }

    // The "unhappy path" is the most valuable test: proving something is NOT allowed.
    // This is the rule that protects your app. If someone later fumbles the enum,
    // this test screams before it reaches production.
    @Test
    void viewer_cannotEditOrDelete() {
        var permissions = ProjectRole.VIEWER.getPermissions();

        // doesNotContain = "make sure this is absent"
        assertThat(permissions).doesNotContain(
                ProjectPermission.EDIT,
                ProjectPermission.DELETE
        );
    }

    @Test
    void editor_canEditAndDelete_butCannotManageMembers() {
        var permissions = ProjectRole.EDITOR.getPermissions();

        assertThat(permissions).contains(ProjectPermission.EDIT, ProjectPermission.DELETE);
        assertThat(permissions).doesNotContain(ProjectPermission.MANAGE_MEMBERS);
    }

    // Only OWNER manages members. This asserts the exact full set,
    // so if anyone accidentally adds/removes a permission, the test catches it.
    @Test
    void owner_hasAllManagementPermissions() {
        var permissions = ProjectRole.OWNER.getPermissions();

        assertThat(permissions).containsExactlyInAnyOrder(
                ProjectPermission.VIEW,
                ProjectPermission.EDIT,
                ProjectPermission.DELETE,
                ProjectPermission.MANAGE_MEMBERS,
                ProjectPermission.VIEW_MEMBERS
        );
    }

    @Test
    void onlyOwner_canManageMembers() {
        assertThat(ProjectRole.OWNER.getPermissions()).contains(ProjectPermission.MANAGE_MEMBERS);
        assertThat(ProjectRole.EDITOR.getPermissions()).doesNotContain(ProjectPermission.MANAGE_MEMBERS);
        assertThat(ProjectRole.VIEWER.getPermissions()).doesNotContain(ProjectPermission.MANAGE_MEMBERS);
    }
}
