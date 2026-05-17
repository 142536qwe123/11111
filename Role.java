public class Role {
    private String roleName;

    public Role() {
        this.roleName = "Unknown";
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // 根据角色输出不同说话内容
    public void roleTalk() {
        switch (roleName) {
            case "Student":
                System.out.println("Hi, how is your homework going?");
                break;
            case "Teacher":
                System.out.println("Hi, how is your paper going?");
                break;
            case "ElderTeacher":
                System.out.println("Hi, how is your research going?");
                break;
            default:
                System.out.println("Hi, how is it going?");
        }
    }
}