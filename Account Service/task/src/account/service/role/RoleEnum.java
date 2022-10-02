package account.service.role;

public enum RoleEnum {
    ROLE_USER(RoleType.BUSINESS),
    ROLE_ACCOUNTANT(RoleType.BUSINESS),
    ROLE_AUDITOR(RoleType.BUSINESS),
    ROLE_ADMINISTRATOR(RoleType.ADMINISTRATIVE);

    private final RoleType type;

    RoleEnum(RoleType type) {
        this.type = type;
    }

    public RoleType getType() {
        return type;
    }
}
