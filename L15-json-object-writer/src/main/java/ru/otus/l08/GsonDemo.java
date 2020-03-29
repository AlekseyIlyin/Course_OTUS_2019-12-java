package ru.otus.l08;

import com.google.gson.Gson;
import ru.otus.l08.diyGson.DiyGson;
//

public class GsonDemo {
    public static void main(String[] args) {
        DiyGson diyGson = new DiyGson();
        //Gson diyGson = new Gson();
        ObjectForTesting obj = new ObjectForTesting(22, "test", 10);
        System.out.println("\nSave object");
        System.out.println(obj);

        String json = diyGson.toJson(obj);
        System.out.println("json:");
        System.out.println(json);

        ObjectForTesting obj2 = new Gson().fromJson(json, ObjectForTesting.class);
        System.out.println("\nObjects equals: " + obj.equals(obj2));
        System.out.println("\nRestore object:");
        System.out.println(obj2);
    }
}
