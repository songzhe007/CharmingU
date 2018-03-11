package zhe.charmu.FriendRecommendation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import zhe.charmu.model.User;

/**
 *    a
 *  /   \
 * d     b
 *  \   /
 *    c
 *    int path =0 ;
 *    dfs(......,path)
 *    if(path==3) .... return;
 *    dfs(...., path+1)
 *    degreeMap.put(c, degreeMap.getOrDefault(c,0)+1);
 *
 *    class instance{
 *      private int index;
 *      private int[] datas;
 *      public instance()
 *    }
 *    generateDatainstance(degreeMap){
 *
 *    }
 */

public class FriendRecommendation {
    private static Map<User, Integer> map;

    public static Map<User,Integer> getRecommendation(User user){
        map = new HashMap<>();

        User prev = null;
        DFS(user, prev, user, 0);
        return map;
    }

    private static void DFS( User currUser, User prev, User targetUser, int path){
        if(path==3) {
            if (prev != null && currUser.friend.contains(targetUser)) {
                map.put(prev,map.getOrDefault(prev,0)+1);
                return;
            }
            return ;
        }
        prev = currUser;
        for(User next : currUser.friend){
            if(next.equals(targetUser)) continue;
            DFS(next, prev, targetUser, path+1);
        }
        return;

    }

    /**
     * Generate the datamatrix for further similarity calculating process.
     * @param candidateMap
     * @return
     */

    public List<Instances> getDataMartix(Map<User, Integer> candidateMap){
        List<Instances> dataMatrix = new LinkedList<>();

        for(Map.Entry<User,Integer> entry : candidateMap.entrySet()){
            User candidate = entry.getKey();
            dataMatrix.add(new Instances(candidate.getCalroies(), (double) entry.getValue(),candidate ));
        }
        return dataMatrix;

    }

    /**
     * Return the top 3 friend recommendations.
     * @param dataMatrix
     * @param Curruser
     * @return
     */

    public static List<User> getFinalCandidates(List<Instances> dataMatrix, User Curruser){

        Instances curr = new Instances(Curruser.getCalroies(), 1.0, Curruser);

        Map<Double, List<User>> sim = new HashMap<>();

        for(Instances data : dataMatrix){
            double numeratorSum = 0.0;
            double denominatorSum = 0.0;

            double calCos = data.getCal()*curr.getCal();
            double degreeCos = data.getDegree()*curr.getDegree();
            numeratorSum+= (calCos+degreeCos);
            denominatorSum+=(Math.sqrt(data.getCal()*data.getCal()+data.getDegree()*data.getDegree())*
                    Math.sqrt(curr.getCal()*curr.getCal()+curr.getDegree()*curr.getDegree()));
            double key = numeratorSum/denominatorSum;
            if(!sim.containsKey(key)){
                sim.put(key, new LinkedList<>());
            }
            sim.get(key).add(data.getUser());
        }

        PriorityQueue<Map.Entry<Double, List<User>>> pq = new PriorityQueue<> (
                new Comparator<Map.Entry<Double,List<User>>>(){
            @Override
            public int compare (Map.Entry<Double, List<User>> m1, Map.Entry<Double, List<User>> m2){
                if(m1.getKey()<m2.getKey()){
                    return -1;
                } else if(m1.getKey()>m2.getKey()){
                    return 1;
                } else{
                    return 0;
                }
            }

        });
        for(Map.Entry<Double,List<User>> entry : sim.entrySet()){
            pq.offer(entry);
            if(pq.size()>3){
                pq.poll();
            }
        }
        List<User> res = new LinkedList<>();
        while(!pq.isEmpty()){
            List<User> candidates = pq.poll().getValue();
            for(User candidate : candidates){
                res.add(candidate);
            }
        }
        return res;
    }
}
