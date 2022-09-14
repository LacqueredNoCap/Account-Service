package account.service.role;

import java.util.Arrays;

public enum RoleType {
    ADMINISTRATIVE,
    BUSINESS;

    public static boolean isCompatibleTypes(RoleType... types) {
        boolean containsOnlyAdminTypes = Arrays.stream(types)
                .allMatch(type -> type.equals(RoleType.ADMINISTRATIVE));
        boolean containsOnlyBusinessTypes = Arrays.stream(types)
                .allMatch(type -> type.equals(RoleType.BUSINESS));

        return containsOnlyAdminTypes || containsOnlyBusinessTypes;
    }
}
