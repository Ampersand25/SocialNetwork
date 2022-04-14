package main.service;

import main.domain.FriendshipByID;
import main.domain.Graph;
import main.domain.User;
import main.exceptions.RepositoryException;
import main.exceptions.ValidationException;
import main.repository.database.FriendshipDbRepository;
import main.repository.database.UserDbRepository;
import main.validator.FriendshipByIDValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class FriendshipService {
    private final FriendshipDbRepository friendshipRepository;
    private final UserDbRepository userRepository;
    private final FriendshipByIDValidator friendshipByIDValidator;
    private int IDcounter;

    /**
     * Constructor
     * @param friendshipRepository the repository that stores friendships
     * @param userRepository the repository that stores users
     * @param friendshipByIDValidator the validator of friendships
     */
    public FriendshipService(FriendshipDbRepository friendshipRepository, UserDbRepository userRepository, FriendshipByIDValidator friendshipByIDValidator) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendshipByIDValidator = friendshipByIDValidator;
        this.IDcounter = getMaxID() + 1;
    }

    /**
     * Gets the maximum ID of the friendships in the repository
     * @return maximum ID of the friendships in the repository
     */
    private Integer getMaxID() {
        Collection<FriendshipByID> friendships = (Collection<FriendshipByID>) friendshipRepository.getAll();
        if (friendships.isEmpty())
            return -1;
        else {
            int maxID = 0;
            for (FriendshipByID friendship : friendships) {
                if (friendship.getID() > maxID)
                    maxID = friendship.getID();
            }
            return maxID;
        }
    }

    /**
     * Adds a friendship
     * @param firstUserID ID of one of the users
     * @param secondUserID the other user's ID
     * @throws ValidationException if friendship's attributes are not valid
     */
    public void addFriendship(Integer firstUserID, Integer secondUserID) throws ValidationException {
        FriendshipByID friendshipByID = new FriendshipByID(IDcounter, firstUserID, secondUserID, LocalDate.now());
        friendshipByIDValidator.validate(friendshipByID);
        friendshipRepository.save(friendshipByID);
        IDcounter++;
    }

    /**
     * Deletes a friendship
     * @param friendshipID ID of the friendship to be deleted
     * @throws RepositoryException if there is no friendship having this ID in the repository
     * @throws IOException if the file cannot be found/opened
     */
    public void deleteFriendship(Integer friendshipID) throws RepositoryException, IOException {
        friendshipRepository.delete(friendshipID);
    }

    /**
     * Deletes all the friendships of a user, by its ID
     * @param userID ID of the user whose friends are to be deleted
     * @throws RepositoryException if a friendship/user cannot be deleted from the repository
     * @throws IOException if the file cannot be found/opened
     */
    public void deleteFriendshipsByUserID(Integer userID) throws RepositoryException, IOException {
        Collection<FriendshipByID> friendshipsByID = (Collection<FriendshipByID>) friendshipRepository.getAll();
        for (FriendshipByID friendshipByID : friendshipsByID) {
            if (Objects.equals(friendshipByID.getFirstUserID(), userID) || Objects.equals(friendshipByID.getSecondUserID(), userID)) {
                deleteFriendship(friendshipByID.getID());
                deleteFriendshipsByUserID(userID);
                break;
            }
        }
    }

    /**
     * Get a collection of all friendships (by ID) in the repository and converts them to real friendships (ID -> User by its ID)
     * @return a collection of all real friendships in the repository
     */
    public Collection<FriendshipByID> getAllFriendships() {
        return (Collection<FriendshipByID>) friendshipRepository.getAll();
    }

    /**
     *
     * @param vf
     * @param visited
     * @param mat
     * @param n
     */
    private void dfsVisit(int vf, boolean[] visited, boolean[][] mat, int n) {
        visited[vf] = true;

        for(int i = 1; i <= n; ++i) {
            if(!mat[vf][i])
                continue;

            if(!visited[i])
                dfsVisit(i, visited, mat, n);
        }
    }

    /**
     *
     * @param mat
     * @param n
     * @return
     */
    private int dfs(boolean[][] mat, int n) {
        boolean[] visited = new boolean[n + 1];

        for(int i = 1; i <= n; ++i)
            visited[i] = false;

        int cont = 0;

        for(int i = 1; i <= n; ++i) {
            if(userRepository.getOne(i) == null)
                continue;

            if(!visited[i]) {
                ++cont;
                dfsVisit(i, visited, mat, n);
            }
        }

        return cont;
    }

    /**
     *
     * @return
     */
    public Integer countCommunities() {
        Collection<FriendshipByID> collection = (Collection<FriendshipByID>) friendshipRepository.getAll();
        List<FriendshipByID> friendships = collection.stream().toList();

        Graph graph = new Graph(friendships, userRepository.getAll());

        boolean[][] mat = graph.getAdj();
        int n = graph.getN();

        return dfs(mat, n);
    }

    /**
     *
     * @param src
     * @param visited
     * @param mat
     * @param n
     * @param listNew
     * @return
     */
    private int bfsVisit(int src, boolean[] visited, boolean[][] mat, int n, List<Integer> listNew) {
        int curr = 1;

        LinkedList<Integer> queue = new LinkedList<Integer>();

        visited[src] = true;
        queue.add(src);
        listNew.add(src);

        while(!queue.isEmpty()) {
            int vf = queue.poll();

            for(int i = 1; i <= n; ++i) {
                if(!mat[vf][i])
                    continue;

                if(!visited[i]) {
                    queue.add(i);
                    listNew.add(i);
                    visited[i] = true;
                    ++curr;
                }
            }
        }

        return curr;
    }

    /**
     *
     * @param mat
     * @param n
     * @return
     */
    private List<Integer> bfs(boolean[][] mat, int n) {
        List<Integer> list = new ArrayList<>();

        boolean[] visited = new boolean[n + 1];

        for(int i = 1; i <= n; ++i)
            visited[i] = false;

        int max = 1;

        for(int i = 1; i <= n; ++i) {
            if(userRepository.getOne(i) == null)
                continue;

            if(!visited[i]) {
                List<Integer> listNew = new ArrayList<>();
                int curr = bfsVisit(i, visited, mat, n, listNew);

                if(curr > max) {
                    max = curr;

                    list.clear();
                    list.addAll(listNew);
                }
            }
        }

        return list;
    }

    /**
     *
     * @return
     */
    public List<Integer> getMostActiveCommunity() {
        Collection<User> usersCollection = (Collection<User>) userRepository.getAll();

        if(usersCollection.isEmpty())
            return new ArrayList<>();

        Collection<FriendshipByID> friendshipsCollection = (Collection<FriendshipByID>) friendshipRepository.getAll();

        if(friendshipsCollection.isEmpty()) {
            List<Integer> list = new ArrayList<>();

            for(User user : usersCollection) {
                list.add(user.getID());

                break;
            }

            return list;
        }

        List<FriendshipByID> friendships = friendshipsCollection.stream().toList();

        Graph graph = new Graph(friendships, userRepository.getAll());

        boolean[][] mat = graph.getAdj();
        int n = graph.getN();

        return bfs(mat, n);
    }

    public Month getMonthConst(Integer monthID) throws ValidationException {
        if(monthID < 1 || monthID > 12)
            throw new ValidationException("Invalid month!\n");

        List<Month> months = new ArrayList<>();
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);

        return months.get(monthID - 1);
    }

    public List<FriendshipByID> getFriendshipsOfUserFromMonth(Integer userID, Integer monthID) throws RepositoryException, ValidationException {
        if(userID <= 0)
            throw new RepositoryException("Invalid id!\n");

        if(userRepository.getOne(userID) == null)
            throw new RepositoryException("User doesn't exist in social network!\n");

        Month month = getMonthConst(monthID);
        List<FriendshipByID> friendships = (List<FriendshipByID>) friendshipRepository.getAll();

        return friendships
                .stream()
                .filter(friendshipByID -> (friendshipByID.getFirstUserID().equals(userID) || friendshipByID.getSecondUserID().equals(userID)))
                .filter(friendshipByID -> friendshipByID.getDate().getMonth().equals(month))
                .toList();
    }
}
