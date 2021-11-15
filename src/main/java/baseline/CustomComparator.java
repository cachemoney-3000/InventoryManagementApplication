package baseline;

import java.util.Comparator;

public class CustomComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2){
        // This method will help sort the value of an item
        // This is done by comparing 2 consecutive values in the table
        if(o1 == null && o2 == null) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;

        Integer i1=null;
        try{ i1=Integer.valueOf(o1); } catch(NumberFormatException ignored){}
        Integer i2=null;
        try{ i2=Integer.valueOf(o2); } catch(NumberFormatException ignored){}

        if(i1==null && i2==null) return o1.compareTo(o2);
        if(i1==null) return -1;
        if(i2==null) return 1;

        return i1-i2;
    }
}
