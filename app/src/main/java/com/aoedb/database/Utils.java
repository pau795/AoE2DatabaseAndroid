package com.aoedb.database;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aoedb.R;
import com.aoedb.adapters.TypeAdapter;
import com.aoedb.data.Entity;
import com.aoedb.data.TypeElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class Utils {

    public static String getProperDescription(String itemDescription, String type){
        String[] d = itemDescription.split("\\|");
        if (d.length > 1){
            switch (type){
                case Database.UNIT: return d[0];
                case Database.BUILDING: return d[1];
                case Database.TECH: return d[2];
            }
        }
        return itemDescription;
    }

    public static String getStatString(double bStat, double cStat, boolean addition, boolean accuracy){
        if (Double.isNaN(bStat)) return "-"; //stat has no value
        else if( Double.compare(bStat, cStat) == 0){ //showing base stat
            BigDecimal b= new BigDecimal(cStat);
            b = b.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
            if (b.compareTo(BigDecimal.ZERO) == 0) return "0";
            else return  b.toPlainString();
        }
        else { // showing base stat + calculated stat (addition changes the format)
            BigDecimal base, calculated;
            String baseString, calculatedString;

            base = new BigDecimal(bStat);
            baseString = getDecimalString(base, 2);

            if (addition) calculated = new BigDecimal(cStat - bStat);
            else calculated = new BigDecimal(cStat);
            calculatedString = getDecimalString(calculated, 2);

            if (accuracy){
                baseString += "%";
                calculatedString += "%";
            }

            if (addition) return baseString + " (+" + calculatedString + ")";
            else return baseString+ " ("+ calculatedString+")";
        }
    }


    public static String getDecimalString(double number, int numDecimals){
        if (Double.isNaN(number)) return "-";
        else if (Double.isInfinite(number)) return "∞";
        else {
            BigDecimal b = new BigDecimal(number);
            return getDecimalString(b, numDecimals);
        }
    }

    private static String getDecimalString(BigDecimal b, int decimals){
        b = b.setScale(decimals, RoundingMode.HALF_UP).stripTrailingZeros();
        if (b.compareTo(BigDecimal.ZERO) == 0) return "0";
        else return b.toPlainString();
    }

    public static int getResourceIcon(String resource){
        switch (resource){
            case Database.WOOD: return R.drawable.r_wood;
            case Database.FOOD: return R.drawable.r_food;
            case Database.GOLD: return R.drawable.r_gold;
            case Database.STONE: return R.drawable.r_stone;
            default: return -1;
        }
    }

    public static int getResourceString(String resource){
        switch (resource){
            case Database.WOOD: return R.string.res_wood;
            case Database.FOOD: return R.string.res_food;
            case Database.GOLD: return R.string.res_gold;
            case Database.STONE: return R.string.res_stone;
            default: return -1;
        }
    }



    public static double calculate(double stat, double value, String operator){
        switch (operator){
            case "+": return stat + value;
            case "-": return stat - value;
            case "*": return stat * value;
            case "/": return stat / value;
            case "@": return value;
            default: return stat;
        }
    }

    public static String getMaxAge(Entity e1, Entity e2){
        String e1Name = e1.getAgeElement().getName(), e2Name = e2.getAgeElement().getName();
        if (e1.getEntityID() == 12 || e1.getEntityID() == 15) e1Name = Database.getElement(Database.TECH_LIST, -1).getName();
        if (e2.getEntityID() == 12 || e2.getEntityID() == 15) e2Name = Database.getElement(Database.TECH_LIST, -1).getName();

        String darkAge = Database.getAppContext().getString(R.string.dark_age);
        String feudalAge = Database.getAppContext().getString(R.string.feudal_age);
        String castleAge = Database.getAppContext().getString(R.string.castle_age);
        String imperialAge = Database.getAppContext().getString(R.string.imperial_age);
        
        if (e1Name.equals(imperialAge) || e2Name.equals(imperialAge)) return imperialAge;
        else if (e1Name.equals(castleAge) || e2Name.equals(castleAge)) return castleAge;
        else if (e1Name.equals(feudalAge) || e2Name.equals(feudalAge)) return feudalAge;
        else return darkAge;
    }

    public static String getDescriptionNameFile(String file){
        if (file.contains(Database.UNIT)) return Database.UNIT_LIST;
        else if (file.contains(Database.BUILDING)) return Database.BUILDING_LIST;
        else if (file.contains(Database.TECH)) return  Database.TECH_LIST;
        else return "";
    }

    public static String unCamelCase(String s){
        String aux = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " ");
        return  aux.substring(0, 1).toUpperCase() + aux.substring(1);
    }

    public static int findSpinnerPosition(int civ, HashMap<Integer, String> civRelation, List<String> civNames){
        String name = civRelation.get(civ);
        int l = 0, r = civNames.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            int res = name.compareTo(civNames.get(m));
            if (res == 0) return m;
            if (res > 0) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }

    public static int convertAge(String name){
            if (name.equals(Database.getAppContext().getString(R.string.dark_age)))return 0;
            else if (name.equals(Database.getAppContext().getString(R.string.feudal_age)))return 1;
            else if (name.equals(Database.getAppContext().getString(R.string.castle_age)))return 2;
            else if (name.equals(Database.getAppContext().getString(R.string.imperial_age)))return 3;
            else return -1;
    }

    public static String getUTCiv(int id){
       return Database.getElement(Database.CIVILIZATION_LIST, Database.getTechnology(id).getAvailableCivIds().get(0)).getName();
    }



    public static String getEntityTypeString(String listString){
        switch (listString){
            case Database.UNIT_LIST: return Database.UNIT;
            case Database.BUILDING_LIST: return Database.BUILDING;
            case Database.TECH_LIST: return Database.TECH;
            case Database.CIVILIZATION_LIST: return Database.CIV;
            case Database.CLASS_LIST: return Database.CLASS;
            case Database.TYPE_LIST: return Database.TYPE;
            case Database.PERFORMANCE_LIST: return Database.PERFORMANCE;
            case Database.HISTORY_LIST: return Database.HISTORY;
            default: return Database.EMPTY;
        }
    }


    public static int mapAgeID(int id){
        switch (id){
            case -1: return 0;
            case 2: return 1;
            case 19: return 2;
            case 90: return 3;
            default: return -1;
        }
    }

    public static String getEffectFileName(String file){
        switch (file){
            case Database.TECH_LIST: return Database.TECH_EFFECT;
            case Database.HIDDEN_BONUS: return Database.HIDDEN_BONUS_EFFECT;
            default: return Database.BONUS_EFFECT;
        }
    }

    public static void showPopupIcon(View parent, Context c, String name, int imageID, boolean transparency, String backgroundColor){
            if (imageID != R.drawable.t_white) {
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.icon_zoom, null);
                TextView nameText = view.findViewById(R.id.entity_name);
                nameText.setText(name);
                View layout = view.findViewById(R.id.popup_layout);
                ImageView icon = view.findViewById(R.id.entity_icon);
                icon.setImageResource(imageID);
                switch (backgroundColor){
                    case Database.BLUE:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back4));
                        icon.setBackgroundColor(c.getColor(R.color.blue_border));
                        break;
                    case Database.RED:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back3));
                        icon.setBackgroundColor(c.getColor(R.color.red_border));
                        break;
                    case Database.GREEN:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back1));
                        icon.setBackgroundColor(c.getColor(R.color.green_border));
                        break;
                }

                if (transparency) icon.setBackground(null);
                final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(true);
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
                View container = pw.getContentView().getRootView();
                Context context = pw.getContentView().getContext();
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(container, p);
            }
    }

    public static void setTypeValues(LinkedHashMap<String, List<TypeElement>> values, ExpandableListView elv, Context c){
        ArrayList<String> groupList = new ArrayList<>(values.keySet());
        TypeAdapter adapter = new TypeAdapter(c, groupList, values);
        elv.setAdapter(adapter);
        for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
    }



    /*
    public static void printOrderedLists(){
        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        List<EntityElement> b = Database.getList(Database.BUILDING_LIST);
        List<EntityElement> t = Database.getList(Database.TECH_LIST);
        Comparator<EntityElement> comparator = new Comparator<EntityElement>() {
            @Override
            public int compare(EntityElement o1, EntityElement o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(u, comparator);
        Collections.sort(b, comparator);
        Collections.sort(t, comparator);
        for(EntityElement e: u) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
        for(EntityElement e: b) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
        for(EntityElement e: t) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
    }


    public static void xml(Context c){
        try{
            LinkedHashMap<Integer, String> history =  new LinkedHashMap<>();
            List<ListElement> list = readList(Database.HISTORY_LIST, c);
            for (ListElement l : list) history.put(l.getId(), readHistory(l.getId(), c));
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag("","list");
            for (int i : history.keySet()){
                String text = history.get(i);
                serializer.startTag("", "item");
                serializer.attribute("", "id", String.valueOf(i));
                serializer.text(text);
                serializer.endTag("", "item");
            }
            serializer.endTag("","list");
            serializer.endDocument();
            FileOutputStream fileos= c.openFileOutput("history_text.xml", Context.MODE_PRIVATE);
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    */
}


