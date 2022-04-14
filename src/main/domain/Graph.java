package main.domain;

import java.util.List;

public class Graph {

    private boolean[][] adj;
    private int n;

    /**
     *
     * @param value
     */
    private void setMatrix(boolean value) {
        for(int i = 1; i <= n; ++i)
            for(int j = 1; j <= n; ++j)
                adj[i][j] = value;
    }

    /**
     *
     * @param objects
     */
    private void initMatrix(Iterable objects) {
        for(Object object : objects) {
            FriendshipByID friendship = (FriendshipByID) object;

            int id1, id2;
            id1 = friendship.getFirstUserID();
            id2 = friendship.getSecondUserID();

            adj[id1][id2] = adj[id2][id1] = true;
        }
    }

    /**
     *
     * @param friendships
     * @param users
     */
    public Graph(List<FriendshipByID> friendships, Iterable<User> users) {
        for(User user : users) {
            int id = user.getID();

            if(id > n)
                n = id;
        }

        adj = new boolean[n + 1][n + 1];

        setMatrix(false);

        initMatrix(friendships);
    }

    /**
     *
     * @return
     */
    public boolean[][] getAdj() {
        return adj;
    }

    /**
     *
     * @return
     */
    public int getN() {
        return n;
    }
}