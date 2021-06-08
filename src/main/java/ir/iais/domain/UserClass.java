package ir.iais.domain;

import com.github.sbahmani.jalcal.util.JalCal;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import ir.iais.authentication.BasicAuthenticationSession;
import ir.iais.db.AbsDBObject;
import ir.iais.db.HibernateUtil;
import ir.iais.db.Transaction;
import ir.iais.needToDelete.PersianDigitConvertor;
import ir.iais.needToDelete.RolePropLoader;
import ir.iais.offlinevalidator.StringValidator;
import org.apache.wicket.util.io.IClusterable;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author vahid
 * create on 6/6/2021
 */
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "meliCode")
        }
)
@SuppressWarnings("unchecked")
public class UserClass extends AbsDBObject implements Serializable, IClusterable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String phoneNumber;
    private String userRole;
    private String meliCode;
    private String name;
    private Long birthDate;
    private String email;
    private Boolean forceChangePassWord;

    //initialize for migration to enable user
    private Boolean enabled = true;
    private Set<Integer> locations;
    private Set<String> roles;
    private Long ssoUpdateTime;
    private SSOToken accessToken;
    private SSOToken refreshToken;
    private String sSOSubject;
    private String fatherName;

    public UserClass() {
    }

    public String getUsername() {
        return username;
    }

    public UserClass(
            String username,
            String password,
            String phoneNumber,
            String userRole,
            String meliCode,
            String name,
            Set<Integer> locations,
            Set<String> roles,
            Long birthDate,
            String email
    ) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.meliCode = meliCode;
        this.name = name;
        this.locations = locations;
        this.roles = roles;
        this.birthDate = birthDate;
        this.email = email;
        this.forceChangePassWord = false;
    }

    public UserClass(
            String username,
            String password,
            String phoneNumber,
            String userRole,
            String meliCode,
            String name,
            Set<Integer> locations,
            Set<String> roles
    ) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.meliCode = meliCode;
        this.name = name;
        this.locations = locations;
        this.roles = roles;
        this.birthDate = null;
        this.email = null;
        this.forceChangePassWord = false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getForceChangePassWord() {
        if (forceChangePassWord == null) {
            return false;
        }
        return forceChangePassWord;
    }

    public void setForceChangePassWord(Boolean forceChangePassWord) {
        this.forceChangePassWord = forceChangePassWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserClass loadByUsername(String username, String password) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.username =:username AND"
                    + " t.password=:password").
                    setParameter("username", username).
                    setParameter("password", password).uniqueResult();
        }
        return user;
    }

    public static UserClass loadByUsername(String username) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.username =:username").
                    setParameter("username", username).uniqueResult();
        }
        return user;
    }

    public static UserClass loadByCodeMeli(String codeMeli) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.meliCode =: m").
                    setParameter("m", codeMeli).uniqueResult();
        }
        return user;
    }

    public static UserClass loadByUsernameAndCodeMelli(String username, String codeMelli) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.username =:username AND"
                    + " t.meliCode=:meliCode").
                    setParameter("username", username).
                    setParameter("meliCode", codeMelli).uniqueResult();
        }
        return user;
    }

    public static void updateOldUser(UserClass user, String cellphone, String firstName, String lastName, String accessToken, String refreshToken, String subject) {
        Transaction t = new Transaction() {

            @Override
            protected void bodyTrx(Session session) {
                UserClass newUser = (UserClass) session.createQuery("select t "
                        + "from UserClass t "
                        + "where t.username =:username").
                        setParameter("username", user.getUsername()).uniqueResult();
                newUser.setPhoneNumber(cellphone);
                newUser.setName(firstName + " " + lastName);
                newUser.setSsoUpdateTime(System.currentTimeMillis());
                newUser.setsSOSubject(subject);
                newUser.setAccessToken(new SSOToken(accessToken));
                newUser.setRefreshToken(new SSOToken(refreshToken));
                session.update(newUser);
            }
        };
        t.doTrx(Transaction.READ_COMMITTED);
    }

    public static UserClass loadBySSOSubject(String subject) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.sSOSubject =:subject").
                    setParameter("subject", subject).uniqueResult();
        }
        return user;
    }

    public static UserClass loadByUsernameAndCodeMelli(String nameField, Set<String> notShownUser) {
        UserClass user;
        Query<UserClass> param;
        String query = " from UserClass t where ";
        if (StringValidator.isNumeric(nameField)) {
            query += " t.meliCode =: meliCode AND t.username not in (:users)";
        } else {
            query += " t.name =: name OR t.username =:username AND t.username not in (:users) ";
        }
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            param = openSession.createQuery(query);
            if (StringValidator.isNumeric(nameField)) {
                param.setParameter("meliCode", nameField);
            } else {
                param.setParameter("name", nameField);
                param.setParameter("username", nameField);
            }
            param.setParameterList("users", notShownUser);

            user = param.uniqueResult();
        }
        return user;
    }

    public static List<UserClass> loadByRole(String role, int pageSize, int nowPage) {
        List<UserClass> users;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            users = openSession.
                    createQuery(
                            "select t FROM UserClass t WHERE :role IN elements(t.roles)"
                    ).
                    setParameter("role", role).
                    setFirstResult((nowPage - 1) * pageSize).
                    setMaxResults(pageSize).
                    list();
        }
        return users;
    }

    public static List<UserClass> loadByNameUserNameLocationRole(Integer pagesize, Integer nowpage, String selectedrole, String namefield, Integer currentlocation) {
        List<UserClass> users;
        String query = "SELECT t FROM UserClass t";
        Query<UserClass> param;
        String currentUserRole = "";
        if (RolePropLoader.getInstance().getFilterUserRole()) {
            currentUserRole = BasicAuthenticationSession.getLiveUser().getUserRole();
        }
        boolean allRoles = currentlocation != 0 | !namefield.equals("") | !selectedrole.equals("AllRoles");
        if (allRoles || !currentUserRole.isEmpty()) {
            query += " WHERE ";
        }

        if (currentUserRole != null && !currentUserRole.isEmpty()) {
            query += allRoles
                    ? " t.userRole=:userRole AND " : " t.userRole=:userRole ";
        }
        if (!selectedrole.equals("AllRoles")) {
            query += currentlocation != 0 | !namefield.equals("") ? " :role IN elements(t.roles) AND " : " :role IN elements(t.roles) ";
        }
        if (currentlocation != 0) {
            query += !namefield.equals("") ? " :loc IN elements(t.locations) AND " : " :loc IN elements(t.locations) ";
        }
        if (!namefield.equals("")) {
            if (StringValidator.isNumeric(namefield)) {
                query += " t.meliCode =: meliCode ";
            } else {
                query += " t.name LIKE :name ";
            }
        }
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            param = openSession.createQuery(query);

            if (!selectedrole.equals("AllRoles")) {
                param.setParameter("role", selectedrole);
            }
            if (currentlocation != 0) {
                param.setParameter("loc", currentlocation);
            }
            if (!namefield.equals("")) {
                if (StringValidator.isNumeric(namefield)) {
                    param.setParameter("meliCode", namefield);
                } else {
                    param.setParameter("name", "%" + namefield + "%");
                }
            }
            if (currentUserRole != null && !currentUserRole.isEmpty()) {
                param = param.setParameter("userRole", currentUserRole);
            }
            if (pagesize != null) {
                param = param.setFirstResult((nowpage - 1) * pagesize).setMaxResults(pagesize);
            }
            users = param.list();
        }
        return users;
    }

    public static List<UserClass> loadAll(Integer pageSize, Integer nowPage) {
        return loadByNameUserNameLocationRole(pageSize, nowPage, "AllRoles", "", 0);
    }

    public static List<UserClass> loadAll() {
        return loadAll(null, null);
    }

    public static boolean isUsernameAvailable(String username) {
        UserClass user;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            user = (UserClass) openSession.createQuery("select t "
                    + "from UserClass t "
                    + "where t.username =:username ").
                    setParameter("username", username).uniqueResult();
        }
        return (user != null);
    }

    public static List<UserClass> loadByName(String name, int pageSize, int nowPage) {
        List<UserClass> users;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            users = (List<UserClass>) openSession.createQuery("select t FROM UserClass t WHERE t.name LIKE :name").setParameter("name", "%" + name + "%").setFirstResult((nowPage - 1) * pageSize).setMaxResults(pageSize).list();
        }
        return users;
    }

    public static List<UserClass> loadByNameAndRole(String name, String role, int pageSize, int nowPage) {
        List<UserClass> users;
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            users = (List<UserClass>) openSession.createQuery("select t FROM UserClass t WHERE :role IN elements(t.roles) AND t.name LIKE :name")
                    .setParameter("name", "%" + name + "%").setParameter("role", role).setFirstResult((nowPage - 1) * pageSize).setMaxResults(pageSize).list();
        }
        return users;
    }

    public static Integer SizeOfLoadAllUsers(String name, String role) {
        Long count;
        String currentUserRole = "";
        if (RolePropLoader.getInstance().getFilterUserRole()) {
            currentUserRole = BasicAuthenticationSession.getLiveUser().getUserRole();
        }
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            String queryStr = "select COUNT(t.dBId) "
                    + "from UserClass t ";
            String str = "";
            if (currentUserRole != null && !currentUserRole.isEmpty()) {
                str = "t.userRole =:userRole";
            }
            if (name != null && !name.isEmpty()) {
                if (!str.isEmpty()) {
                    str += " AND ";
                }
                str += "t.name LIKE :name";
            }
            if (role != null && !role.isEmpty()) {
                if (!str.isEmpty()) {
                    str += " AND ";
                }
                str += ":role IN elements(t.roles)";
            }
            if (!str.isEmpty()) {
                queryStr += "WHERE " + str;
            }
            Query<Long> createQuery = openSession.createQuery(queryStr);
            if (name != null && !name.isEmpty()) {
                createQuery = createQuery.setParameter("name", "%" + name + "%");
            }
            if (role != null && !role.isEmpty()) {
                createQuery = createQuery.setParameter("role", role);
            }
            if (currentUserRole != null && !currentUserRole.isEmpty()) {
                createQuery = createQuery.setParameter("userRole", currentUserRole);
            }
            count = createQuery.uniqueResult();
        }
        if (count == null) {
            count = 0L;
        }
        return count.intValue();
    }

    public static String Diff(UserClass newUser, UserClass oldUser) {
        StringBuilder compareStr = new StringBuilder("for " + newUser.getUsername() + " ");
        DiffNode diff = ObjectDifferBuilder.buildDefault().compare(newUser, oldUser);
        diff.visit((DiffNode node, Visit visit) -> {
            if (node.hasChanges() && !node.hasChildren()) {
                Object baseValue = node.canonicalGet(oldUser);
                Object changedValue = node.canonicalGet(newUser);
                final String message = node.getPath() + " changed ";
                String[] matches = new String[]{"date", "time"};
                for (String s : matches) {
                    if (node.getPropertyName().toLowerCase().contains(s)) {
                        try {
                            if (baseValue != null) {
                                baseValue = PersianDigitConvertor.convertToPersianDigit(
                                        JalCal.gregorianToJalali(new Date((long) baseValue), false));
                            }
                            if (changedValue != null) {
                                changedValue = PersianDigitConvertor.convertToPersianDigit(
                                        JalCal.gregorianToJalali(new Date((long) changedValue), false));
                                break;
                            }
                        } catch (NullPointerException | NumberFormatException ignored) {
                        }
                    }
                }
                if (!node.getPath().toString().equals("/password")) {
                    compareStr.append(message).append(" from ").append(baseValue).append(" to ").append(changedValue);
                } else {
                    compareStr.append(message);
                }
                compareStr.append(" | ");
            }
        });
        return compareStr.toString();
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.username);
        hash = 53 * hash + Objects.hashCode(this.password);
        hash = 53 * hash + Objects.hashCode(this.phoneNumber);
        hash = 53 * hash + Objects.hashCode(this.userRole);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserClass other = (UserClass) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        return Objects.equals(this.userRole, other.userRole);
    }

    @Override
    public void update() {
        Transaction transaction = new Transaction() {

            @Override
            protected void bodyTrx(Session session) {
                session.update(UserClass.this);
            }
        };
        transaction.doTrx(Transaction.SERIALIZABLE);
    }

    public String getMeliCode() {
        return meliCode;
    }

    public void setMeliCode(String meliCode) {
        this.meliCode = PersianDigitConvertor.convertFromPersianDigit(meliCode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserClass{" + "username=" + username + ", userRole=" + userRole + ", meliCode=" + meliCode + ", name=" + name + ", enabled=" + enabled + ", locations=" + locations + ", roles=" + roles + '}';
    }

    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @Column
    @Fetch(FetchMode.JOIN)
    public Set<Integer> getLocations() {
        return locations;
    }

    public void setLocations(Set<Integer> locations) {
        this.locations = locations;
    }

    @Transient
    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column
    @Fetch(FetchMode.JOIN)
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getSsoUpdateTime() {
        return ssoUpdateTime;
    }

    public void setSsoUpdateTime(Long ssoUpdateTime) {
        this.ssoUpdateTime = ssoUpdateTime;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public SSOToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(SSOToken accessToken) {
        this.accessToken = accessToken;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public SSOToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(SSOToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getsSOSubject() {
        return sSOSubject;
    }

    public void setsSOSubject(String sSOSubject) {
        this.sSOSubject = sSOSubject;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
