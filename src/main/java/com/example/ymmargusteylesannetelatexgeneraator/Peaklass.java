package com.example.ymmargusteylesannetelatexgeneraator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Peaklass extends Application {
    private static final List<String> teemadeNimed = Arrays.asList("Järjestatud korpuse omadused", "Alumine ja ülemine raja", "Arvjadad. Jada piirväärtuse definitsioon ja omadused", "Jada piirväärtuse omadused ja arvutamine", "Monotoonsusprintsiip. Cauchy kriteerium. Osajadad", "Alumine ja ülemine piirväärtus", "Funktsiooni piirväärtuse definistioon ja omadused", "Funktsiooni piirväärtuse arvutamine. Pidevus", "Elementaarfunktsioonid. Pöördfunktsioon", "Ekvivalentsed suurused. Landau sümbolid", "Katkevad funktsioonid. Ühtlane pidevus");

    @Override
    public void start(Stage peaLava) throws IOException {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        List<Teema> teemaList = looTeemad();
        BorderPane piiriPaan = new BorderPane();

        peaLava.setTitle("Teema/Teemade valik");
        ToggleButton[] nupud = new ToggleButton[teemaList.size()];
        Text ülemineTekst = new Text("Vali sobivad teemad");

        for (int i = 0; i < nupud.length; i++) {
            nupud[i] = new ToggleButton();
            vBox.getChildren().add(nupud[i]);
            nupud[i].setText(teemaList.get(i).getNimi());
        }

        Button kinnitusNupp = new Button("Kinnita valik");
        piiriPaan.setTop(ülemineTekst);
        piiriPaan.setCenter(vBox);
        piiriPaan.setBottom(kinnitusNupp);

        Map<String, Boolean> valitudNupud = new HashMap<>();
        List<Teema> teemad = new ArrayList<>();

        kinnitusNupp.setOnAction(event1 -> {
            for (ToggleButton toggleButton : nupud) {
                if (toggleButton.isSelected()) {
                    valitudNupud.put(toggleButton.getText(), Boolean.TRUE);
                    Teema vaadeldav = teemaList.get(teemadeNimed.indexOf(toggleButton.getText()));
                    teemad.add(vaadeldav);
                }
            }
            if (valitudNupud.isEmpty()) throw new KäivitusErind("Ühtegi teemat ei valitud!");
            vBox.getChildren().clear();

            peaLava.setTitle("Arvutus/Tõestus");
            Text ülemineTekst2 = new Text("Kas soovid arvutus- või tõestusülesandeid?");
            RadioButton arvutus = new RadioButton("Arvutus");
            RadioButton tõestus = new RadioButton("Tõestus");
            looNupud(vBox, piiriPaan, kinnitusNupp, ülemineTekst2, arvutus, tõestus); // et vältida koodi kordamist

            kinnitusNupp.setOnAction(event2 -> {
                int olemasolevateÜlesanneteArv = 0;
                if (arvutus.isSelected()) {
                    for (Teema teema : teemad) {
                        olemasolevateÜlesanneteArv += teema.getArvutusÜlesanded().size();
                    }
                } else if (tõestus.isSelected()) {
                    for (Teema teema : teemad) {
                        olemasolevateÜlesanneteArv += teema.getTõestusÜlesanded().size();
                    }
                } else throw new KäivitusErind("Ühtegi ülesande tüüpi ei valitud!");

                if (olemasolevateÜlesanneteArv == 0)
                    throw new KäivitusErind("Valitud teemadele ei leitud ühtegi ülesannet!");

                peaLava.setTitle("Ülesanded");
                piiriPaan.getChildren().remove(kinnitusNupp);
                Text ülemineTekst3 = new Text("Kirjuta ülesannete arv ja vajuta 'ENTER'; vastavaid ülesandeid on kokku " + olemasolevateÜlesanneteArv + " tükki");
                TextField ülesanneteArv = new TextField();

                piiriPaan.setTop(ülemineTekst3);
                vBox.getChildren().addAll(ülesanneteArv);
                piiriPaan.setCenter(vBox);

                int finalOlemasolevateÜlesanneteArv = olemasolevateÜlesanneteArv;
                ülesanneteArv.setOnKeyPressed(event3 -> {
                    if (event3.getCode() == KeyCode.ENTER) {
                        int sisestatudArv = Integer.parseInt(ülesanneteArv.getText());
                        List<Ülesanne> ülesanded = new ArrayList<>();
                        List<Ülesanne> tagastatavadÜlesanded = new ArrayList<>();
                        if (sisestatudArv > finalOlemasolevateÜlesanneteArv || sisestatudArv < 1)
                            throw new KeelatudArgumendiErind("Sisestatud arv ei sobi!");
                        for (Teema teema : teemad) {
                            if (arvutus.isSelected()) {
                                ülesanded.addAll(teema.getArvutusÜlesanded());
                            } else if (tõestus.isSelected()) {
                                ülesanded.addAll(teema.getTõestusÜlesanded());
                            }
                        }
                        Collections.shuffle(ülesanded);
                        for (int i = 0; i < sisestatudArv; i++) {
                            tagastatavadÜlesanded.add(ülesanded.get(i));
                        }

                        vBox.getChildren().clear();

                        peaLava.setTitle("PDF/TXT");
                        Text ülemineTekst4 = new Text("Kas soovid PDF- või TXT-faili?");
                        RadioButton pdfNupp = new RadioButton("PDF");
                        RadioButton txtNupp = new RadioButton("TXT");
                        looNupud(vBox, piiriPaan, kinnitusNupp, ülemineTekst4, pdfNupp, txtNupp);

                        kinnitusNupp.setOnAction(event4 -> {
                            if (pdfNupp.isSelected()) {
                                try {
                                    genereeriPdf(genereeriTekst(tagastatavadÜlesanded));
                                } catch (IOException e) {
                                    throw new RuntimeException(e); // ei näe probleemi selles lahenduses
                                }
                            } else if (txtNupp.isSelected()) {
                                try {
                                    genereeriTxt(genereeriTekst(tagastatavadÜlesanded));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else throw new KäivitusErind("Ühtegi failitüüpi ei valitud!");

                            peaLava.setTitle("Valmis!");
                            Text tekst = new Text("Fail on loodud! Mida soovid teha?");
                            Button sulgeProgramm = new Button("Sulge programm");
                            Button avaFail = new Button("Ava fail");

                            piiriPaan.setCenter(tekst);
                            piiriPaan.setBottom(sulgeProgramm);
                            piiriPaan.setTop(avaFail);
                            sulgeProgramm.setOnAction(event5 -> Platform.exit());
                            if (txtNupp.isSelected()) {
                                avaFail.setOnAction(event6 -> getHostServices().showDocument(".\\väljund.txt"));
                            } else if (pdfNupp.isSelected()) {
                                avaFail.setOnAction(event6 -> getHostServices().showDocument(".\\väljund.pdf"));
                            }
                        }); // event4
                    } // if event3 == ENTER
                }); // event3
            }); // event2
        }); // event1
        peaLava.setScene(new Scene(piiriPaan, 500, 500));
        peaLava.show();
    }

    public static void teade(String sõnum) {
        Stage teade = new Stage();
        teade.setTitle("Teade");
        BorderPane piiriPaan2 = new BorderPane();
        piiriPaan2.setCenter(new Text(sõnum));
        Scene stseen2 = new Scene(piiriPaan2, 300, 100);
        teade.setScene(stseen2);
        teade.show();
    }

    private static void looNupud(VBox verticalBox, BorderPane piiriPaan, Button kinnitusNupp, Text ülemineTekst, RadioButton nupp1, RadioButton nupp2) {
        ToggleGroup toggleGroup = new ToggleGroup();
        nupp1.setToggleGroup(toggleGroup);
        nupp2.setToggleGroup(toggleGroup);

        piiriPaan.setTop(ülemineTekst);
        verticalBox.getChildren().addAll(nupp1, nupp2);
        piiriPaan.setCenter(verticalBox);
        piiriPaan.setBottom(kinnitusNupp);
    }

    public static void main(String[] args) {
        launch();
    }

    private static void genereeriPdf(String tekst) throws IOException {
        String document = "\\documentclass{article}\n" + "\\usepackage[utf8]{inputenc}\n" + "\\usepackage[estonian]{babel}\n" + "\\usepackage{amsmath}\n" + "\\usepackage{amssymb}\n" + "\\usepackage[letterpaper,top=2cm,bottom=2cm,left=3cm,right=3cm,marginparwidth=1.75cm]{geometry}\n" + "\\usepackage{amsthm}\n" + "\\usepackage{mathtools}\n" + "\\usepackage{amsfonts}\n" + "\\usepackage{mathptmx}\n" + "\\everymath{\\displaystyle}\n" + "\\linespread{1.6}\n" + "\\begin{document}\n" + tekst + "\n" + "\\end{document}";

        String fileName = "väljund.tex";
        try (Writer out = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            out.write(document);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // kole aga ei tohiks juhtuda
        }

        String os = System.getProperty("os.name").toLowerCase();
        String[] cmd;
        if (os.contains("win")) {
            cmd = new String[]{"cmd.exe", "/c", String.format("pdflatex %s", fileName)};
        } else {
            cmd = new String[]{"/bin/bash", "-c", String.format("pdflatex %s", fileName)};
        }
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader lugeja = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String rida;
            while ((rida = lugeja.readLine()) != null) {
                System.out.println(rida);
            }
        } catch (IOException e) {
            e.printStackTrace(); // meil pole vaja et midagi teeks vist otseselt, las olla nii
        }
    }

    private static void genereeriTxt(String tekst) throws IOException {
        try (Writer out = new OutputStreamWriter(new FileOutputStream("väljund.txt"), StandardCharsets.UTF_8)) {
            out.write(tekst);
        }
    }

    private static String genereeriTekst(List<Ülesanne> ülesanded) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ülesanded.size(); i++) {
            sb.append(i + 1).append(". ").append(ülesanded.get(i).toString()).append("\n\n");
        }
        return sb.toString();
    }

    private static List<Teema> looTeemad() throws IOException {
        List<Teema> teemad = new ArrayList<>();
        for (String nimi : Peaklass.teemadeNimed) teemad.add(new Teema(nimi));
        return loeÜlesanded(teemad);
    }

    private static List<Teema> loeÜlesanded(List<Teema> teemad) throws IOException {
        java.io.File fail = new java.io.File("ylesanded.txt");
        try (Scanner sc = new Scanner(fail, StandardCharsets.UTF_8)) {
            for (Teema teema : teemad) {
                while (sc.hasNextLine()) {
                    String rida = sc.nextLine();
                    if (rida.equals("-----")) break;
                    String[] reaOsad = rida.split("; ");
                    teema.lisaÜlesanne(reaOsad[0], reaOsad[1], reaOsad[2]);
                }
            }
        }
        return teemad;
    }

}
