package sse.com.moneyalertsse.menu.statistique.diagramme;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import sse.com.moneyalertsse.R;
import sse.com.moneyalertsse.baseDeDonnee.DaoBase.ClasseDao.DepenseDao;
import sse.com.moneyalertsse.baseDeDonnee.DaoBase.ClasseDao.GainDao;
import sse.com.moneyalertsse.baseDeDonnee.DaoBase.ClasseDao.TypeDepenseDao;
import sse.com.moneyalertsse.baseDeDonnee.DaoBase.ClasseDao.TypeGainDao;

import static sse.com.moneyalertsse.menu.statistique.StatistiqueDepense.depenseInutile;
import static sse.com.moneyalertsse.menu.statistique.StatistiqueDepense.depenseUtile;
import static sse.com.moneyalertsse.menu.statistique.StatistiqueGain.epargne;
import static sse.com.moneyalertsse.menu.statistique.StatistiqueGain.pret;
import static sse.com.moneyalertsse.menu.statistique.StatistiqueGain.salaire;

/**
 * Created by cedricakrou on 07/11/18.
 * cette classe sert à
 */

public class DiagrammeEnBande<T>
{

    private BarChart diagrammeEnBande = null;
    private Context context = null;
    List<String> listeLibelle = null;


    private String titleDiagramme = "";


    public DiagrammeEnBande(BarChart BarChart, Context contextDiagramme)
    {
        diagrammeEnBande = BarChart;
        context = contextDiagramme ;
    }

    /**
     * fonction permettant d'afficher le diagramme EnBande en fonction des differents parametres
     * @param percent
     * @param tableSelectionne
     * @param categorie . ce parametre est vide si on souhaite afficher les statistiques de gain
     * @return
     */

    public synchronized BarChart ConstructionDiagrammeEnBande(Boolean percent, final T tableSelectionne, final String categorie)
    {

        diagrammeEnBande.setDrawBarShadow(false);
        diagrammeEnBande.getDescription().setEnabled(true);
        diagrammeEnBande.setExtraOffsets(5, 10, 5, 5);
        diagrammeEnBande.animateY(1000, Easing.EasingOption.EaseInBack);

        diagrammeEnBande.setDragDecelerationFrictionCoef(0.95f);


        final List<BarEntry> BarEntries = new ArrayList<BarEntry>();


                if( tableSelectionne instanceof DepenseDao)
                {

                    titleDiagramme = context.getResources().getString(R.string.texteDepense);


                    if(categorie.equals("Affichage par categorie"))
                    {
                        final DepenseDao depenseDao = new DepenseDao(context);

                        depenseDao.ouvrirBaseDonnee();
                        final double utile = depenseDao.SommeChamp(depenseUtile);
                        final double inutile = depenseDao.SommeChamp(depenseInutile);
                        BarEntries.add(new BarEntry((float) utile, 0));
                        BarEntries.add(new BarEntry((float) inutile, 1));
                        depenseDao.fermerBaseDeDonnee();


                    }
                    else
                    {
                        final List<String> listeTypeDepense = new ArrayList<String>();

                        /**
                         * processus pour construire les types de gain dans le Dao
                         */

                                TypeDepenseDao typeDepenseDao = new TypeDepenseDao(context);

                                typeDepenseDao.ouvrirBaseDonnee();

                                for (int i = 0; i < typeDepenseDao.afficherTousLesObjets().size(); i++)
                                {
                                    listeTypeDepense.add(typeDepenseDao.afficherTousLesObjets().get(i).getNomTypeDepense());
                                }

                                typeDepenseDao.fermerBaseDeDonnee();

                                listeLibelle = listeTypeDepense;


                        /**
                         * processus pour construire le BarEntries
                         */

                                final DepenseDao depenseDao = new DepenseDao(context);

                                depenseDao.ouvrirBaseDonnee();


                                final double sommeTransport = depenseDao.SommeChampByType("transport");
                                final double sommeNourriture = depenseDao.SommeChampByType("nourriture");
                                final double sommeBesoinHygenique = depenseDao.SommeChampByType("besoin hygiénique");

                                int nombreTypeDepense = 0;

                                for(int i=0; i < listeTypeDepense.size(); i++)
                                {
                                    BarEntries.add(new BarEntry((float) depenseDao.SommeChampByType(listeTypeDepense.get(i)), i ));
                                    nombreTypeDepense = i;
                                }

                                BarEntries.add(new BarEntry(nombreTypeDepense + 1 , (float) sommeTransport ));
                                BarEntries.add(new BarEntry(nombreTypeDepense + 2, (float) sommeNourriture));
                                BarEntries.add(new BarEntry(nombreTypeDepense + 3, (float) sommeBesoinHygenique ));


                                depenseDao.fermerBaseDeDonnee();


                    }


                }
                else if(tableSelectionne instanceof GainDao)
                {

                    titleDiagramme = context.getResources().getString(R.string.texteGain);


                    final List<String> listeTypeGain = new ArrayList<String>();

                    /**
                     * processus pour construire les types de gain dans le Dao
                     */


                            TypeGainDao typeGainDao = new TypeGainDao(context);

                            typeGainDao.ouvrirBaseDonnee();

                            for (int i = 0; i < typeGainDao.afficherTousLesObjets().size(); i++)
                            {
                                listeTypeGain.add(typeGainDao.afficherTousLesObjets().get(i).getNomTypeGain());
                            }

                            typeGainDao.fermerBaseDeDonnee();

                            listeLibelle = listeTypeGain;



                    /**
                     * processus pour construire le BarEntries
                     */

                            final GainDao gainDao = new GainDao(context);

                            gainDao.ouvrirBaseDonnee();

                            final double sommeSalaire = gainDao.SommeChamp(salaire);
                            final double sommePret = gainDao.SommeChamp(pret);
                            final double sommeEpargne = gainDao.SommeChamp(epargne);


                            int taille = 0;

                            for(int i=0; i < listeTypeGain.size(); i++)
                            {
                                BarEntries.add(new BarEntry((float) gainDao.SommeChamp(listeTypeGain.get(i)), i));
                                taille = i;

                                Toast.makeText(context, "" + listeTypeGain.size(), Toast.LENGTH_SHORT).show();
                            }

                            BarEntries.add(new BarEntry(taille +1, (float) sommeSalaire ));
                            BarEntries.add(new BarEntry(taille + 2, (float) sommePret ));
                            BarEntries.add(new BarEntry(taille + 3, (float) sommeEpargne ));


                            gainDao.fermerBaseDeDonnee();


                }


                BarDataSet dataSet = new BarDataSet(BarEntries, titleDiagramme);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);


                BarData data = new BarData(dataSet);
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.RED);
                data.setBarWidth(30f);

                data.setValueFormatter(new LargeValueFormatter());


                Legend l = diagrammeEnBande.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(true);
                l.setXOffset(20f);
                l.setYOffset(20f);
                l.setYEntrySpace(0f);
                l.setTextSize(8f);

                // x-axis et y-axis

        // x-axis

        XAxis xAxis = diagrammeEnBande.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
     //   xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(listeLibelle));


        // y-axis

        diagrammeEnBande.getAxisRight().setEnabled(false);
        YAxis leftAxis = diagrammeEnBande.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMaximum(0f);

        diagrammeEnBande.setData(data);

        diagrammeEnBande.notifyDataSetChanged();


        return diagrammeEnBande;
    }


}
