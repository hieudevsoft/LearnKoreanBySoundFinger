package com.devapp.modoulewritehand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dinh Sam Vu on 1/24/2021.
 */
public class GoogleTranslateJSONObject {
    @SerializedName("sentences")
    @Expose
    private List<Sentence> sentences = null;
    @SerializedName("dict")
    @Expose
    private List<Dict> dict = null;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("synsets")
    @Expose
    private List<Synset> synsets = null;
    @SerializedName("related_words")
    @Expose
    private RelatedWords relatedWords;
    @SerializedName("alternative_translations")
    @Expose
    private List<Alternative> alternative = null;


    public List<Alternative> getAlternative() {
        return alternative;
    }

    public void setAlternative(List<Alternative> alternative) {
        this.alternative = alternative;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public List<Dict> getDict() {
        return dict;
    }

    public void setDict(List<Dict> dict) {
        this.dict = dict;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<Synset> getSynsets() {
        return synsets;
    }

    public void setSynsets(List<Synset> synsets) {
        this.synsets = synsets;
    }

    public RelatedWords getRelatedWords() {
        return relatedWords;
    }

    public void setRelatedWords(RelatedWords relatedWords) {
        this.relatedWords = relatedWords;
    }

    public class Dict {

        @SerializedName("pos")
        @Expose
        private String pos;
        @SerializedName("terms")
        @Expose
        private List<String> terms = null;
        @SerializedName("entry")
        @Expose
        private List<Entry> entry = null;
        @SerializedName("base_form")
        @Expose
        private String baseForm;
        @SerializedName("pos_enum")
        @Expose
        private Integer posEnum;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<String> getTerms() {
            return terms;
        }

        public void setTerms(List<String> terms) {
            this.terms = terms;
        }

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> entry) {
            this.entry = entry;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

        public Integer getPosEnum() {
            return posEnum;
        }

        public void setPosEnum(Integer posEnum) {
            this.posEnum = posEnum;
        }
    }

    public class Entry {

        @SerializedName("word")
        @Expose
        private String word;
        @SerializedName("reverse_translation")
        @Expose
        private List<String> reverseTranslation = null;
        @SerializedName("score")
        @Expose
        private Double score;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public List<String> getReverseTranslation() {
            return reverseTranslation;
        }

        public void setReverseTranslation(List<String> reverseTranslation) {
            this.reverseTranslation = reverseTranslation;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

    }

    public class Entry_ {

        @SerializedName("synonym")
        @Expose
        private List<String> synonym = null;
        @SerializedName("definition_id")
        @Expose
        private String definitionId;

        public List<String> getSynonym() {
            return synonym;
        }

        public void setSynonym(List<String> synonym) {
            this.synonym = synonym;
        }

        public String getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(String definitionId) {
            this.definitionId = definitionId;
        }

    }

    public class RelatedWords {

        @SerializedName("word")
        @Expose
        private List<String> word = null;

        public List<String> getWord() {
            return word;
        }

        public void setWord(List<String> word) {
            this.word = word;
        }

    }

    public static class Sentence {

        @SerializedName("trans")
        @Expose
        private String trans;
        @SerializedName("orig")
        @Expose
        private String orig;
        @SerializedName("backend")
        @Expose
        private Integer backend;
        @SerializedName("src_translit")
        @Expose
        private String srcTranslit;

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public Integer getBackend() {
            return backend;
        }

        public void setBackend(Integer backend) {
            this.backend = backend;
        }

        public String getSrcTranslit() {
            return srcTranslit;
        }

        public void setSrcTranslit(String srcTranslit) {
            this.srcTranslit = srcTranslit;
        }

    }

    public class Synset {

        @SerializedName("pos")
        @Expose
        private String pos;
        @SerializedName("entry")
        @Expose
        private List<Entry_> entry = null;
        @SerializedName("base_form")
        @Expose
        private String baseForm;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<Entry_> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry_> entry) {
            this.entry = entry;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

    }

    public class Alternative {
        String src_phrase = null;

        @SerializedName("alternative")
        @Expose
        List<Alter> alter = null;

        public String getSrc_phrase() {
            return src_phrase;
        }

        public void setSrc_phrase(String src_phrase) {
            this.src_phrase = src_phrase;
        }

        public List<Alter> getAlter() {
            return alter;
        }

        public void setAlter(List<Alter> alter) {
            this.alter = alter;
        }


    }

    public class Alter {
        String word_postproc = null;

        public String getWord_postproc() {
            return word_postproc;
        }

        public void setWord_postproc(String word_postproc) {
            this.word_postproc = word_postproc;
        }
    }
}
