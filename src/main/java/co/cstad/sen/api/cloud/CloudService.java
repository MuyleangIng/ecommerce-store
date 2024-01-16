package co.cstad.sen.api.cloud;

public interface CloudService {

    boolean createUser(String username, String email, String password);

    boolean updateUserEmailByUsername(String username, String email);

}
