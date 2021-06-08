package ir.iais.auditing;

import com.github.sbahmani.jalcal.util.JalCal;
import ir.iais.authentication.BasicAuthenticationSession;
import ir.iais.db.AbsDBObject;
import ir.iais.db.HibernateUtil;
import ir.iais.domain.UserClass;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.io.IClusterable;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author leila
 */
@Entity
@Table(
        indexes = {
                @Index(name = "AuditLogEventTimestampIndex", columnList = "eventTimestamp")
        })
public class AuditLog extends AbsDBObject implements Serializable, IClusterable {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AuditLog.class);

    private String IP;
    private Long eventTimestamp;
    private UserClass whoDid;
    private String message;
    private AuditPriority priority;
    private Integer code;

    public AuditLog() {
    }

    public AuditLog(String IP, Long eventTimestamp, UserClass whoDid, String message, AuditPriority priority, Integer code) {
        this.IP = IP;
        this.eventTimestamp = eventTimestamp;
        this.whoDid = whoDid;
        this.message = message;
        this.priority = priority;
        this.code = code;
    }

    public static void LOG(String message, AuditPriority priority, Integer code) {

        try {
            String IP = getCurrentUserIP();
            UserClass currentUser = getCurrentUser();
            AuditLog.SaveAuditLogWithFields(IP, currentUser, message, priority, code);
        } catch (RuntimeException e) {
            LOGGER.fatal("error audit log", e);
        }
    }

    public static void RESTLOG(String IP, UserClass currentUser, String message, AuditPriority priority, Integer code) {

        try {
            AuditLog.SaveAuditLogWithFields(IP, currentUser, message, priority, code);
        } catch (RuntimeException e) {
            LOGGER.fatal("error audit log", e);
        }
    }

    public static AuditLog SaveAuditLogWithFields(String IP, UserClass user, Long timestamp, String message, AuditPriority priority, Integer code) {
        AuditLog newAuditLog = new AuditLog(IP, timestamp, user, message, priority, code);
        newAuditLog.save();
        LOGGER.info("From " + IP + " User " + user
                + " Date " + JalCal.gregorianToJalaliDate(new Date(timestamp), true)
                + " Priority " + priority
                + " Code " + code
                + "\n" + message);
        return newAuditLog;
    }

    public static AuditLog SaveAuditLogWithFields(String IP, UserClass user, String message, AuditPriority priority, Integer code) {
        return SaveAuditLogWithFields(IP, user, System.currentTimeMillis(), message, priority, code);
    }

    public static List<AuditLog> LoadByUserTimePriorityCode(UserClass user, Long start, Long end, List<AuditPriority> priority, List<Integer> code, String ip, String message, int pageSize, int nowPage) {
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT l FROM AuditLog l";
            Map<String, Object> params = new HashMap<>();
            query = createQueryForUserTimePriority(user, start, end, priority, code, query, params, ip, message);
            query += " ORDER BY l.eventTimestamp DESC";
            Query<AuditLog> createQuery = openSession.
                    createQuery(query, AuditLog.class)
                    .setFirstResult((nowPage - 1) * pageSize)
                    .setMaxResults(pageSize);
            createQuery.getParameterMetadata().getNamedParameterNames().forEach((str) -> {
                switch (str) {
                    case "priority":
                        createQuery.setParameterList(str, priority);
                        break;
                    case "code":
                        createQuery.setParameterList(str, code);
                        break;
                    case "IP":
                        createQuery.setParameter(str, ip + "%");
                        break;
                    case "message":
                        createQuery.setParameter(str, "%" + message + "%");
                        break;
                    default:
                        createQuery.setParameter(str, params.get(str));
                        break;
                }
            });
            return createQuery.list();
        }
    }

    public static Long CounterOfLoadByUserTimePriorityCode(UserClass user, Long start, Long end, List<AuditPriority> priority, List<Integer> code, String ip, String message) {
        try (Session openSession = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT count(l) FROM AuditLog l";
            Map<String, Object> params = new HashMap<>();
            query = createQueryForUserTimePriority(user, start, end, priority, code, query, params, ip, message);
            Query<Long> createQuery = openSession.createQuery(query);
            createQuery.getParameterMetadata().getNamedParameterNames().forEach((str) -> {
                switch (str) {
                    case "priority":
                        createQuery.setParameterList(str, priority);
                        break;
                    case "code":
                        createQuery.setParameterList(str, code);
                        break;
                    case "IP":
                        createQuery.setParameter(str, ip + "%");
                        break;
                    case "message":
                        createQuery.setParameter(str, "%" + message + "%");
                        break;
                    default:
                        createQuery.setParameter(str, params.get(str));
                        break;
                }
            });
            return createQuery.uniqueResult();
        }
    }

    @SuppressWarnings("GrazieInspection")
    private static String createQueryForUserTimePriority(UserClass user, Long start, Long end, List<AuditPriority> priority, List<Integer> code, String query, Map<String, Object> params, String ip, String message) {
        if (user != null || start != null || end != null || priority != null || code != null || message != null) {
            query += " WHERE 1=1";
        }
        if (user != null) {
            query += " and  l.whoDid =:user";
            params.put("user", user);
        }
        if (start != null) {
            query += " and l.eventTimestamp >=:start";
            params.put("start", start);
        }
        if (end != null) {
            query += " and l.eventTimestamp <=:end";
            params.put("end", end);
        }
        if (priority != null && !priority.isEmpty()) {
            query += " and l.priority in :priority";
        }
        if (code != null && !code.isEmpty()) {
            query += " and l.code in :code";
        }

        if (ip != null && !ip.isEmpty()) {
            query += " and l.IP LIKE :IP";
        }
        if (message != null && !message.isEmpty()) {
            query += " and l.message LIKE :message";
        }
        return query;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.IP);
        hash = 23 * hash + Objects.hashCode(this.eventTimestamp);
        hash = 23 * hash + Objects.hashCode(this.whoDid);
        hash = 23 * hash + Objects.hashCode(this.message);
        hash = 23 * hash + Objects.hashCode(this.priority);
        hash = 23 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuditLog other = (AuditLog) obj;
        if (!Objects.equals(this.IP, other.IP)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.eventTimestamp, other.eventTimestamp)) {
            return false;
        }
        if (!Objects.equals(this.whoDid.getUsername(), other.whoDid.getUsername())) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return "AuditLog{" + "IP=" + IP + ", timeStamp=" + eventTimestamp + ", user=" + whoDid + ", message=" + message + ", priority=" + priority + '}';
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public static String getCurrentUserIP() {
        WebRequest req = (WebRequest) RequestCycle.get().getRequest();
        HttpServletRequest httpReq = (HttpServletRequest) req.getContainerRequest();
        return httpReq.getRemoteHost();
    }

    public Long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @ManyToOne
    public UserClass getWhoDid() {
        return whoDid;
    }

    public void setWhoDid(UserClass whoDid) {
        this.whoDid = whoDid;
    }

    public static UserClass getCurrentUser() {
        return BasicAuthenticationSession.getLiveUser();
    }

    @Column(columnDefinition = "TEXT")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuditPriority getPriority() {
        return priority;
    }

    public void setPriority(AuditPriority priority) {
        this.priority = priority;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
