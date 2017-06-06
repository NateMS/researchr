package ch.fhnw.researchr.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public class LanguagePM {
    private final StringProperty applicationTitle  = new SimpleStringProperty("Programmiersprachen");

    private final IntegerProperty selectedLanguageId = new SimpleIntegerProperty(-1);

    private ObservableList<Language> languages = FXCollections.observableArrayList();

    private final Language languageProxy = new Language();

    private final ObservableList<Command> undoStack = FXCollections.observableArrayList();
    private final ObservableList<Command> redoStack = FXCollections.observableArrayList();

    private BooleanProperty disabledUndo = new SimpleBooleanProperty();
    private BooleanProperty disabledRedo = new SimpleBooleanProperty();

    private final ChangeListener propertyChangeListenerForUndoSupport = (observable, oldValue, newValue) -> {
        redoStack.clear();
        undoStack.add(0, new ValueChangeCommand(this, (Property) observable, oldValue, newValue));
    };

    public LanguagePM() {
        this(getLanguages());
    }

    public LanguagePM(List<Language> languageList) {

        languages.addAll(languageList);

        disabledUndo.bind(Bindings.isEmpty(undoStack));
        disabledUndo.bind(Bindings.isEmpty(redoStack));


        selectedLanguageIdProperty().addListener((observable, oldValue, newValue) -> {
                    Language oldSelection = getLanguage(oldValue.intValue());
                    Language newSelection = getLanguage(newValue.intValue());


                    if (oldSelection != null) {
                        unbindFromProxy(oldSelection);
                        disableUndoSupport(oldSelection);
                    }

                    if (newSelection != null) {
                        bindToProxy(newSelection);
                        enableUndoSupport(newSelection);
                    }

                }
        );

        setSelectedLanguageId(1);

        selectedLanguageIdProperty().addListener(propertyChangeListenerForUndoSupport);
    }

    public final Language getLanguageProxy() {
        return languageProxy;
    }

    private Language getLanguage(int id) {
        return languages.stream()
                .filter(Language -> Language.getId() == id)
                .findAny()
                .orElse(null);
    }

    private static List<Language> getLanguages() {
        return Arrays.asList(
                new Language(1, "PHP", 1990, "dude", "ayy", "lmao", 1995),
                new Language(1, "Java", 1990, "dude", "ayy", "lmao", 1995),
                new Language(1, "C#", 1990, "dude", "ayy", "lmao", 1995),
                new Language(1, "C", 1990, "dude", "ayy", "lmao", 1995),
                new Language(1, "Haskell", 1990, "dude", "ayy", "lmao", 1995),
                new Language(1, "Javascript", 1990, "dude", "ayy", "lmao", 1995));
    }

    public ObservableList<Language> languages() {
        return languages;
    }

    public String getApplicationTitle() {
        return applicationTitle.get();
    }

    public StringProperty applicationTitleProperty() {
        return applicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle.set(applicationTitle);
    }

    public int getSelectedLanguageId() {
        return selectedLanguageId.get();
    }

    public IntegerProperty selectedLanguageIdProperty() {
        return selectedLanguageId;
    }

    public void setSelectedLanguageId(int selectedLanguageId) {
        this.selectedLanguageId.set(selectedLanguageId);
    }

    public <T> void setPropertyValueWithoutUndoSupport(Property<T> property, T newValue){
        property.removeListener(propertyChangeListenerForUndoSupport);
        property.setValue(newValue);
        property.addListener(propertyChangeListenerForUndoSupport);
    }

    public void save() {
        Language lang = this.getLanguage(this.getSelectedLanguageId());

        if (lang == null) return;

        System.out.println("Language: " + lang.getName());
    }

    public void addNew() {

    }

    public void remove() {

    }

    public void undo() {
        if (undoStack.isEmpty()) return;

        Command undoCommand = undoStack.remove(0);
        redoStack.add(0, undoCommand);
        undoCommand.undo();
    }

    public void redo() {
        if (redoStack.isEmpty()) return;

        Command redoCommand = redoStack.remove(0);
        undoStack.add(0, redoCommand);
        redoCommand.redo();
    }

    public BooleanProperty disabledUndoProperty() {
        return disabledUndo;
    }

    public BooleanProperty disabledRedoProperty() {
        return disabledRedo;
    }

    private void disableUndoSupport(Language language) {
        language.idProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.nameProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.publishedYearProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.developerProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.typingProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.paradigmsProperty().removeListener(propertyChangeListenerForUndoSupport);
        language.stackoverflowTagsProperty().removeListener(propertyChangeListenerForUndoSupport);
    }

    private void enableUndoSupport(Language language) {
        language.idProperty().addListener(propertyChangeListenerForUndoSupport);
        language.nameProperty().addListener(propertyChangeListenerForUndoSupport);
        language.publishedYearProperty().addListener(propertyChangeListenerForUndoSupport);
        language.developerProperty().addListener(propertyChangeListenerForUndoSupport);
        language.typingProperty().addListener(propertyChangeListenerForUndoSupport);
        language.paradigmsProperty().addListener(propertyChangeListenerForUndoSupport);
        language.stackoverflowTagsProperty().addListener(propertyChangeListenerForUndoSupport);
    }

    private void unbindFromProxy(Language language) {
        languageProxy.idProperty().unbindBidirectional(language.idProperty());
        languageProxy.nameProperty().unbindBidirectional(language.nameProperty());
        languageProxy.publishedYearProperty().unbindBidirectional(language.publishedYearProperty());
        languageProxy.developerProperty().unbindBidirectional(language.developerProperty());
        languageProxy.typingProperty().unbindBidirectional(language.typingProperty());
        languageProxy.paradigmsProperty().unbindBidirectional(language.paradigmsProperty());
        languageProxy.stackoverflowTagsProperty().unbindBidirectional(language.stackoverflowTagsProperty());
    }

    private void bindToProxy(Language language) {
        languageProxy.idProperty().bindBidirectional(language.idProperty());
        languageProxy.nameProperty().bindBidirectional(language.nameProperty());
        languageProxy.publishedYearProperty().bindBidirectional(language.publishedYearProperty());
        languageProxy.developerProperty().bindBidirectional(language.developerProperty());
        languageProxy.typingProperty().bindBidirectional(language.typingProperty());
        languageProxy.paradigmsProperty().bindBidirectional(language.paradigmsProperty());
        languageProxy.stackoverflowTagsProperty().bindBidirectional(language.stackoverflowTagsProperty());
    }
}