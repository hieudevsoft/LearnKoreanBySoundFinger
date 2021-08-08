package com.devapp.modoulewritehand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dinh Sam Vu on 1/24/2021.
 */
public class WordJSONObject {
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private boolean found;

    public boolean getFound() {
        return this.found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    private ArrayList<Datum> data;

    public ArrayList<Datum> getData() {
        return this.data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public static class Mean {

        public Mean(String mean, String kind) {
            this.mean = mean;
            this.kind = kind;
        }

        private String mean;

        public String getMean() {
            return this.mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
        }

        private String kind;

        public String getKind() {
            return this.kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }
    }

    public static class Datum {

        public Datum(boolean isMatches, String _id, String word, String phonetic, String seq, ArrayList<Mean> means) {
            this.isMatches = isMatches;
            this._id = _id;
            this.word = word;
            this.phonetic = phonetic;
            this.seq = seq;
            this.means = means;
        }

        private String phonetic;

        public String getPhonetic() {
            return this.phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        private int weight;

        public int getWeight() {
            return this.weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        private String tag;

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        private String link;

        public String getLink() {
            return this.link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        private ArrayList<Mean> means;

        public ArrayList<Mean> getMeans() {
            return this.means;
        }

        public void setMeans(ArrayList<Mean> means) {
            this.means = means;
        }

        private String label;

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        private String type;

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private ArrayList<String> lang;

        public ArrayList<String> getLang() {
            return this.lang;
        }

        public void setLang(ArrayList<String> lang) {
            this.lang = lang;
        }

        private String _rev;

        public String getRev() {
            return this._rev;
        }

        public void setRev(String _rev) {
            this._rev = _rev;
        }

        private int mobileId;

        public int getMobileId() {
            return this.mobileId;
        }

        public void setMobileId(int mobileId) {
            this.mobileId = mobileId;
        }

        private String _id;

        public String getId() {
            return this._id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        private String short_mean;

        public String getShortMean() {
            return this.short_mean;
        }

        public void setShortMean(String short_mean) {
            this.short_mean = short_mean;
        }

        private String seq;

        public String getSeq() {
            return this.seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        private String word;

        public String getWord() {
            return this.word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        private Boolean isMatches;

        public Boolean isMatches() {
            if (isMatches == null)
                return false;
            return isMatches;
        }

        public void setMatches(Boolean matches) {
            isMatches = matches;
        }

//         MARK: example
        private ExampleJSONObject example;

        public ExampleJSONObject getExample() {
            return example;
        }

        public void setExample(ExampleJSONObject example) {
            this.example = example;
        }

        // MARK: tu dong nghia
        private List<GoogleTranslateJSONObject.Synset> synsetList;

        public List<GoogleTranslateJSONObject.Synset> getSynsetList() {
            return synsetList;
        }

        public void setSynsetArrayList(List<GoogleTranslateJSONObject.Synset> synsetArrayList) {
            this.synsetList = synsetArrayList;
        }

        // MARK: word review item
        private boolean isFavorite;

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }
    }

    /**
     * MARK: sort and check is matches
     */
    private boolean hasMatchesWord;

    public boolean isHasMatchesWord() {
        return hasMatchesWord;
    }

    public void sortAndCheckMatches(String query) {
        ArrayList<Datum> matches = new ArrayList<>();
        ArrayList<Datum> others = new ArrayList<>();
        for (Datum datum : data) {
            if (datum.getWord() != null && datum.getWord().equals(query)) {
                datum.isMatches = true;
                matches.add(datum);
            } else if (datum.getPhonetic() != null && datum.getPhonetic().equals(query)) {
                datum.isMatches = true;
                matches.add(datum);
            } else {
                others.add(datum);
            }
        }

        hasMatchesWord = !matches.isEmpty();
        data.clear();
        data.addAll(matches);
        data.addAll(others);
    }


    public WordJSONObject() {
    }
}