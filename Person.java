public abstract class Person {
    private Name name;
    private int age;
    private Gender gender;
    private Role role;   // 新增：角色对象

    // 无参构造函数
    public Person() {
        this.name = new Name();
        this.age = 0;
        this.gender = null;
        this.role = new Role(); // 默认为 Unknown 角色
    }

    // 有参构造函数（增加 Role 参数）
    public Person(int age, Gender gender, Name name, Role role) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.role = role;
    }

    // 访问器
    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // 修改后的 talk() 方法：委托给 role.roleTalk()
    public void talk() {
        if (role != null) {
            role.roleTalk();
        } else {
            System.out.println("No role assigned.");
        }
    }

    public void talk(String topic) {
        System.out.println("Let's talk about " + topic + ".");
    }

    public void chatWith(Person p, String topic) {
        String aName = this.name.toString();
        String bName = p.name.toString();
        System.out.println(aName + " to " + bName + ": Let's talk about " + topic + ".");
    }

    public abstract void work();

    public final void breathe() {
        System.out.println("Breathing...");
    }

    public Person getPartner() {
        return null;
    }

    @Override
    public String toString() {
        return name.toString() + ", " + age + ", " + gender + ", Role: " + role.getRoleName();
    }
}