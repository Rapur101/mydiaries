package Tables;

import Entries.DefaultEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Main table class
 *
 * @param <T> Table Entry type
 */
public class DefaultTable<T> implements Serializable {

    private List<T> tabelData = new ArrayList<>();


    public DefaultTable(List<T> tabelData) {
        this.tabelData = tabelData;
    }

    public DefaultTable() {
    }

    /**
     * Adds new entry to table.
     *
     * @param data Entry
     */
    public void addData(T data) {
        tabelData.add(data);
    }

    /**
     * Sets tableData field.
     * @param data
     */
    public void setData (List<T> data){
        tabelData = data;
    }

    /**
     * Removes entry from table.
     *
     * @param indeks
     */
    public void removeData(int indeks) {
        tabelData.remove(indeks);
    }


    /**
     * Loads table from JSON file.
     *
     * @param fileName Filename with extension.
     * @throws IOException If file was not found.
     */
    public void loadTable(String fileName) throws IOException {

        //final Type REVIEW_TYPE = new TypeToken<Tables.DefaultTable<T>>() {}.getType();
        final Gson gson = new Gson();

        try (final JsonReader jsonReader = new JsonReader(new FileReader(fileName))) {
            DefaultTable<T> defaultTable = gson.fromJson(jsonReader, this.getClass());
            if (defaultTable == null) return;
            this.tabelData = defaultTable.tabelData;
        }

    }


    /**
     * Saves table to JSON file.
     *
     * @param fileName Filename with extension.
     * @throws IOException If file was not found.
     */
    public void saveTabel(String fileName) throws IOException {
        final Gson gson = new Gson();

        final Type REVIEW_TYPE = new TypeToken<DefaultTable<T>>() {
        }.getType();
        String jsonFile = gson.toJson(this, REVIEW_TYPE);

        try (final FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonFile);
        }


    }


    /**
     * Tabel data Getter.
     *
     * @return ArrayList of Entry T
     */
    public List<T> getTabel() {
        return tabelData;
    }

    /**
     * Table ordered data Getter
     *
     * @param comparator Comparator, that is being used to sort data.
     * @return Ordered ArrayList of Entry T.
     */
    public List<T> getOrderedTable(Comparator<T> comparator) {
        List<T> tempData = new ArrayList<>(tabelData);
        tempData.sort(comparator);
        return tempData;
    }

    /**
     * This method orderes list by Entry-class object variables.
     *
     * @param type Int typenumber
     *             0- type
     *             1- inputdate
     *             2- expDate
     * @param ascending
     * @return
     */
    public List<T> getOrderedTable(int type, boolean ascending){
        return this.getOrderedTable((o1, o2) -> {
            int value;
            switch (type){
                case 0: value = ((DefaultEntry)o1).getType().compareTo(((DefaultEntry)o2).getType());break;
                case 1: value = ((DefaultEntry)o1).getInputDate().compareTo(((DefaultEntry)o2).getInputDate());break;
                default:
                    throw new IllegalArgumentException("DefaultTable#getOrderedTabel wrong ordering type");
            }
            return value  * (ascending ? 1 : -1);
        });
    }

    /**
     * Table entry Getter.
     *
     * @param index Entry indeks.
     * @return Entry T
     */
    public T getEntry(int index) {
        return tabelData.get(index);
    }


}
