import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.WordAnalysis;


public class Main {
    private static final TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
    public static MyHashTable<Integer, User> userHashTable = new MyHashTable<>();
    public static MyHashTable<String, User[]> ilgialanlari = new MyHashTable<>();
    public static MyHashTable<String, String []> bolgeanalizi = new MyHashTable<>();
    public static MyHashTable<String, String[]> dilAnalizi = new MyHashTable<>();
    public static void main(String[] args) {
        String filePath = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\twitter_data.json";

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        StringBuilder jsonContent = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }

        userHashTable = processJson(jsonContent.toString());
        for(int i = 0 ; i < userHashTable.getSize();i++){
            User user = userHashTable.get(i);
            ilgianalizi(user);
            tumilgianalizleri(user);
            bolgeAnalizi(user);
            dilAnalizi(user);
            updateFollowersAndFollowing(user);
        }
        User user1 = userHashTable.get(0);
        user1.relatedFollowers=kullaniciAnalizi(user1);
        MyGraph graph = new MyGraph();
        graph.addVertex(user1.getUsername());
        for(int j = 0 ; j < user1.relatedFollowers.length;j++){
            if(user1.relatedFollowers[j]!= null){
                graph.addEdge(user1.getUsername(), user1.relatedFollowers[j].getUsername());
            }
        }
        User user2 = userHashTable.get(1);
        user2.relatedFollowers=kullaniciAnalizi(user2);
        graph.addVertex(user2.getUsername());
        for(int j = 0 ; j < user2.relatedFollowers.length;j++){
            if(user2.relatedFollowers[j]!= null){
                graph.addEdge(user2.getUsername(), user2.relatedFollowers[j].getUsername());
            }
        }
        System.out.println(graph);
        
        MyHashTable.Entry<String, User[]>[] ilgialanlariTable = ilgialanlari.getTable();
        String outputFilePath = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\UserInterest.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            for (int i = 0; i < userHashTable.getSize(); i++) {
                User user = userHashTable.get(i);
                writer.println("Username: " + user.getUsername());
                writer.println("Interest1: "+user.interest[0]);
                writer.println("Interest2: "+user.interest[1]);
                writer.println("Interest3: "+user.interest[2]);
                writer.println(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFilePath1 = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\UserTweets.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath1))) {
            for (int i = 0; i < userHashTable.getSize(); i++) {
                User user = userHashTable.get(i);
                writer.println("Username: " + user.getUsername());
                writer.println("Interest1: "+user.interest[0]);
                writer.println("Interest2: "+user.interest[1]);
                writer.println("Interest3: "+user.interest[2]);
                    for(int k = 0; k < user.tweets.length; k++){
                        String[] words = user.tweets[k].split("\\s+");
                        for (int l = 0; l < words.length; l++) {
                            String word = words[l];
                            String lemma = extractLemma(word);
                            words[l] = lemma;
                        }
                        if (Arrays.asList(words).contains(user.interest[0]) ||
                            Arrays.asList(words).contains(user.interest[1]) ||
                            Arrays.asList(words).contains(user.interest[2])){
                            writer.println(user.tweets[k]);
                        }
                    }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFilePath2 = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\InterestAnalysis.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath2))) {
            for (MyHashTable.Entry<String, User[]> entry : ilgialanlari.getTable()) {
                if (entry != null) {
                    String ilgiAlani = entry.getKey();
                    User[] kullaniciDizisi = entry.getValue();
                    writer.println("İlgi Alanı: " + ilgiAlani);
                    for (User user : kullaniciDizisi) {
                        // Kullanıcı adını yazdır
                        writer.println("Kullanıcı Adı: " + user.getUsername());
                    }
                    writer.println();
        }
    }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFilePath3 = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\RegionAnalysis.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath3))) {
            for (MyHashTable.Entry<String, String[]> entry : bolgeanalizi.getTable()) {
                if (entry != null) {
                    String bolge = entry.getKey();
                    String[] IlgiAlaniDizisi = entry.getValue();
                    writer.println("Bölge: " + bolge);
                    for (String ilgiAlani : IlgiAlaniDizisi) {
                        if(ilgiAlani!=null){
                        writer.println("İlgi Alanı: " + ilgiAlani);
                        }
                    }
                    writer.println();
        }
    }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFilePath4 = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\LanguageAnalysis.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath4))) {
            for (MyHashTable.Entry<String, String[]> entry : dilAnalizi.getTable()) {
                if (entry != null) {
                    String dil = entry.getKey();
                    String[] IlgiAlaniDizisi = entry.getValue();
                    writer.println("Dil: " + dil);
                    for (String ilgiAlani : IlgiAlaniDizisi) {
                        if(ilgiAlani!=null){
                        writer.println("İlgi Alanı: " + ilgiAlani);
                        }
                    }
                    writer.println();
        }
    }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFilePath5 = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\RelatedFollowers.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath5))) {
            for (int i = 0; i < 2; i++) {
                User user = userHashTable.get(i);
                writer.println("Username: " + user.getUsername());
                    for(int j = 0 ; j < user.relatedFollowers.length;j++){
                        if(user.relatedFollowers[j]!= null){
                        writer.println("Related Followers Username: "+user.relatedFollowers[j].getUsername());
                        }
                    }
                writer.println(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    catch (IOException | ParseException e) {
        e.printStackTrace();
    }
}

    private static MyHashTable<Integer, User> processJson(String json) throws ParseException {
        MyHashTable<Integer, User> userHashTable = new MyHashTable<>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);

       if (obj instanceof JSONArray) {
            JSONArray usersArray = (JSONArray) obj;

            for (int i = 0; i < usersArray.size(); i++) {
                JSONObject userJson = (JSONObject) usersArray.get(i);
                User user = parseUser(userJson);
                userHashTable.put(i, user);
            }
        } else {
            System.out.println("Geçersiz JSON formatı: " + json);
        }
       
        return userHashTable;
    }

    private static User parseUser(JSONObject userJson) {
        User user = new User();
        user.setUsername((String) userJson.get("username"));
        user.setName((String) userJson.get("name"));
        user.setFollowersCount(Math.toIntExact((Long) userJson.get("followers_count")));
        user.setFollowingCount(Math.toIntExact((Long) userJson.get("following_count")));
        user.setLanguage((String) userJson.get("language"));
        user.setRegion((String) userJson.get("region"));
        user.setTweets(parseStringArray((JSONArray) userJson.get("tweets")));
        user.setFollowing(parseStringArray1((JSONArray) userJson.get("following")));
        user.setFollowers(parseStringArray1((JSONArray) userJson.get("followers")));
        return user;
    }
    
    private static void updateFollowersAndFollowing(User user) {
    String[] followers = user.getFollowers();
    String[] following = user.getFollowing();
    if (user.followersUser == null) {
        user.followersUser = new User[followers.length];
    }
    if (user.followingUser == null) {
        user.followingUser = new User[following.length];
    }
    int index = 0;
    for (String followerUsername : followers) {
        User follower = findUserByUsername(followerUsername);
        if (follower != null) {
            user.followersUser[index] = follower;
        }
        index++;
    }
    index = 0;
    for (String followingUsername : following) {
        User followedUser = findUserByUsername(followingUsername);
        if (followedUser != null) {
            user.followingUser[index] = followedUser;
        }
        index++;
    }
}

    private static User findUserByUsername(String username) {
        for (MyHashTable.Entry<Integer, User> entry : userHashTable.getTable()) {
            if (entry != null) {
                User user = entry.getValue();
                if (user != null && user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    private static String[] parseStringArray1(JSONArray jsonArray) {
    String[] stringArray = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
        String tweet = (String) jsonArray.get(i);
        String[] tweetParts = tweet.split(", ");
        stringArray[i] = tweetParts[0]; 
    }
    return stringArray;
}

    private static void printUserDetails(User user) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Name: " + user.getName());
        System.out.println("Followers Count: " + user.getFollowersCount());
        System.out.println("Following Count: " + user.getFollowingCount());
        System.out.println("Language: " + user.getLanguage());
        System.out.println("Region: " + user.getRegion());
        System.out.println("Tweets: " + user.getTweets());
        System.out.println("Following: " + user.getFollowing());
        System.out.println("Followers: " + user.getFollowers());
    }
    private static void printAllUsers(MyHashTable<Integer, User> userHashTable) {
        for (int i = 0; i < userHashTable.getSize(); i++) {
            User user = userHashTable.get(i);
            if (user != null) {
                System.out.println("User " + i + " Details:");
                printUserDetails(user);
            }
        }
    }
    private static String[] parseStringArray(JSONArray jsonArray) {
    String[] stringArray = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
        String element = (String) jsonArray.get(i);
        stringArray[i] = element;
    }
    return stringArray;
}
    public static String extractLemma(String word) {
        WordAnalysis analysis = morphology.analyze(word);
        if (!analysis.getAnalysisResults().isEmpty()) {
            return analysis.getAnalysisResults().get(0).getStem();
        }
        return word;
    }
    
    private static String convertTurkishToEnglish(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized)
                .replaceAll("")
                .replaceAll("ı", "i")
                .replaceAll("İ", "i")
                .replaceAll("ğ", "g")
                .replaceAll("ü", "u")
                .replaceAll("ş", "s")
                .replaceAll("ç", "c")
                .replaceAll("ö", "o");
    }

     
    public static void ilgianalizi(User user) {
    user.interest = new String[3];
    MyHashTable<String, Integer> wordCounts = new MyHashTable<>();
    String[] allWords = new String[user.tweets.length * 10];
    int index = 0;

    for (int i = 0; i < user.tweets.length; i++) {
        String[] words = user.tweets[i].replaceAll("[^a-zA-ZğüşıöçĞÜŞİÖÇ\\s]", "").split("\\s+");
        for (String word : words) {
            allWords[index] = word;
            String lemma = extractLemma(word);
            wordCounts.put(lemma, wordCounts.getOrDefault(lemma, 0) + 1);
            index++;
        }
    }

    for (int j = 0; j < 3; j++) {
        MyHashTable.Entry<String, Integer> maxEntry = wordCounts.findMaxEntry();
        while (maxEntry != null &&
               (maxEntry.getKey().equals(user.interest[0]) ||
                maxEntry.getKey().equals(user.interest[1]) ||
                maxEntry.getKey().equals(user.interest[2]))) {
            wordCounts.remove(maxEntry.getKey());
            maxEntry = wordCounts.findMaxEntry();
        }
        if (maxEntry != null) {
            user.interest[j] = maxEntry.getKey();
            wordCounts.remove(maxEntry.getKey());
        }
    }
}
    public static void tumilgianalizleri(User user) {
    String[] ilgiAlanlari = user.interest;
    for (String ilgiAlani : ilgiAlanlari) {
        if (!ilgialanlari.containsKey(ilgiAlani)) {
            User[] yeniKullaniciDizisi = {user};
            ilgialanlari.put(ilgiAlani, yeniKullaniciDizisi);
        } else {
            User[] mevcutKullaniciDizisi = ilgialanlari.get(ilgiAlani);
            ilgialanlari.remove(ilgiAlani);
            User[] yeniKullaniciDizisi = new User[mevcutKullaniciDizisi.length + 1];
            System.arraycopy(mevcutKullaniciDizisi, 0, yeniKullaniciDizisi, 0, mevcutKullaniciDizisi.length);
            yeniKullaniciDizisi[mevcutKullaniciDizisi.length] = user;
            ilgialanlari.put(ilgiAlani, yeniKullaniciDizisi);
        }
    }
}
    public static void bolgeAnalizi(User user) {
    String bolge = user.region;
    String[] interest =user.interest;
        if (!bolgeanalizi.containsKey(bolge)) {
            String[] yeniIlgiAlaniDizisi = new String[3];
            System.arraycopy(interest, 0, yeniIlgiAlaniDizisi, 0, interest.length);
            bolgeanalizi.put(bolge, yeniIlgiAlaniDizisi);
        } 
        else {
            String[] mevcutIlgiAlaniDizisi = bolgeanalizi.get(bolge);
            bolgeanalizi.remove(bolge);
             String[] yeniIlgiAlaniDizisi = new String[mevcutIlgiAlaniDizisi.length + 3];
             System.arraycopy(mevcutIlgiAlaniDizisi, 0, yeniIlgiAlaniDizisi, 0, mevcutIlgiAlaniDizisi.length);
            int index = mevcutIlgiAlaniDizisi.length;
            for (String yeniIlgiAlani : interest) {
                if (!Arrays.asList(mevcutIlgiAlaniDizisi).contains(yeniIlgiAlani)) {
                    yeniIlgiAlaniDizisi[index++] = yeniIlgiAlani;
                }
            }
            bolgeanalizi.put(bolge, yeniIlgiAlaniDizisi);
        }
    }

    public static void dilAnalizi(User user) {
    String dil = user.language;
    String[] interest =user.interest;
        if (!dilAnalizi.containsKey(dil)) {
            String[] yeniIlgiAlaniDizisi = new String[3];
            System.arraycopy(interest, 0, yeniIlgiAlaniDizisi, 0, interest.length);
            dilAnalizi.put(dil, yeniIlgiAlaniDizisi);
        } else {
            String[] mevcutIlgiAlaniDizisi = dilAnalizi.get(dil);
            dilAnalizi.remove(dil);
            String[] yeniIlgiAlaniDizisi = new String[mevcutIlgiAlaniDizisi.length + 3];
             System.arraycopy(mevcutIlgiAlaniDizisi, 0, yeniIlgiAlaniDizisi, 0, mevcutIlgiAlaniDizisi.length);
            int index = mevcutIlgiAlaniDizisi.length;
        for (String yeniIlgiAlani : interest) {
            if (!Arrays.asList(mevcutIlgiAlaniDizisi).contains(yeniIlgiAlani)) {
                yeniIlgiAlaniDizisi[index++] = yeniIlgiAlani;
            }
        }
            dilAnalizi.put(dil, yeniIlgiAlaniDizisi);
        }
    
    }
    
    public static User[] kullaniciAnalizi(User user) {
    User[] followers = user.followersUser;
    User[] ilgiliFollower = new User[user.getFollowersCount()];
    String[] interest = user.interest;
    int index = 0;
    if (followers != null) {
        for (User user1 : followers) {
            if (user1 != null && user1.interest != null) {
                 ilgianalizi(user1);
                String[] followerInterest =user1.interest;
                    if (followerInterest[0].equals(interest[0]) ||
                        followerInterest[0].equals(interest[1]) ||
                        followerInterest[0].equals(interest[2]) ||
                        followerInterest[1].equals(interest[0]) ||
                        followerInterest[1].equals(interest[1]) ||
                        followerInterest[1].equals(interest[2]) ||    
                        followerInterest[2].equals(interest[0]) ||
                        followerInterest[2].equals(interest[1]) ||
                        followerInterest[2].equals(interest[2])) {
                        ilgiliFollower[index] = user1;
                    }
                index++;
            }
        }
    }
     return ilgiliFollower;
    }

}
