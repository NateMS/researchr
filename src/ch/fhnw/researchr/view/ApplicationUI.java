package ch.fhnw.researchr.view;

import ch.fhnw.researchr.model.Language;
import ch.fhnw.researchr.model.LanguagePM;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;


public class ApplicationUI extends BorderPane implements ViewMixin {
    private final LanguagePM model;

    private Toolbar      toolbar;
    private LanguageForm languageForm;
    private LanguageListView languageListView;
    private SplitPane splitPane;

    public ApplicationUI(LanguagePM model) {
        this.model = model;
        init();
    }

    public void updateListView(ObservableList<Language> e) {
      //  languageListView.setItems()
    }

    @Override
    public void initializeControls() {
        languageForm = new LanguageForm(model);
        toolbar = new Toolbar(model);
        languageListView = new LanguageListView(model);
        splitPane = new SplitPane();
    }

    @Override
    public void layoutControls() {
        setTop(toolbar);

        splitPane.setDividerPositions(0.4f);
        languageListView.minWidthProperty().bind(splitPane.widthProperty().multiply(0.4f));
        splitPane.getItems().addAll(languageListView, languageForm);

        setCenter(splitPane);
        //setCenter(new VBox(countryHeader, countryForm));
    }

    @Override
    public void addBindings() {
        languageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                model.setSelectedLanguageId(newSelection.getId());
            }
        });

        model.selectedLanguageIdProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                languageListView.getSelectionModel().select(model.getLanguage(model.getSelectedLanguageId()));
            }
        });

        toolbar.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            model.setSearchText(newValue);
            languageListView.setItems(model.filtered());
        });


       /* toolbar.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
                model.languages.stream()
                        .filter(language -> language.getName().toLowerCase().contains(newValue.toLowerCase()))
                        .findAny()
                        .ifPresent(language -> {
                            languageListView.getSelectionModel().select(language);
                            languageListView.scrollTo(language);

                        });
        });*/


    }

}
