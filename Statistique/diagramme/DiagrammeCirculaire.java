package sse.com.moneyalertsse.menu.statistique.diagramme;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
 * Created by cedricakrou on 31/10/18.
 * cette classe sert à dessiner le diagramme circualire
 */

public class DiagrammeCirculaire<T>
{
    private PieChart diagrammeCirculaire = null;
    private Context context;

    private String titleDiagramme = "";

    public DiagrammeCirculaire(PieChart pieChart, Context contextDiagramme)
    {
        diagrammeCirculaire = pieChart;
        context = contextDiagramme ;
    }

    /**
     * fonction permettant d'afficher le diagramme circulaire en fonction des differents parametres
     * @param percent
     * @param tableSelectionne
     * @param categorie . ce parametre est vide si on souhaite afficher les statistiques de gain
     * @return
     */

    public synchronized PieChart ConstructionDiagrammeCirculaire(Boolean percent, final T tableSelectionne, final String categorie)
    {

        diagrammeCirculaire.setUsePercentValues(percent);
        diagrammeCirculaire.getDescription().setEnabled(true);
        diagrammeCirculaire.setExtraOffsets(5, 10, 5, 5);
        diagrammeCirculaire.animateY(1000, Easing.EasingOption.EaseInBack);

        diagrammeCirculaire.setDragDecelerationFrictionCoef(0.95f);

        diagrammeCirculaire.setDrawHoleEnabled(true);
        diagrammeCirculaire.setHoleColor(Color.WHITE);
        diagrammeCirculaire.setTransparentCircleRadius(61f);

        final List<PieEntry> pieEntries = new ArrayList<PieEntry>();



                if( tableSelectionne instanceof DepenseDao )
                {

                    titleDiagramme = context.getResources().getString(R.string.texteDepense);

                    if(categorie.equals("Affichage par categorie"))
                    {
                        final DepenseDao depenseDao = new DepenseDao(context);

                        depenseDao.ouvrirBaseDonnee();

                        final double utile = depenseDao.SommeChamp(depenseUtile);
                        final double inutile = depenseDao.SommeChamp(depenseInutile);
                        pieEntries.add(new PieEntry((float) utile, depenseUtile));
                        pieEntries.add(new PieEntry((float) inutile, depenseInutile));

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


                        /**
                         * processus pour construire le pieEntries
                         */

                                final DepenseDao depenseDao = new DepenseDao(context);

                                depenseDao.ouvrirBaseDonnee();


                                final double sommeTransport = depenseDao.SommeChampByType("transport");
                                final double sommeNourriture = depenseDao.SommeChampByType("nourriture");
                                final double sommeBesoinHygenique = depenseDao.SommeChampByType("besoin hygiénique");


                                for(int i=0; i < listeTypeDepense.size(); i++)
                                {
                                    pieEntries.add(new PieEntry((float) depenseDao.SommeChampByType(listeTypeDepense.get(i)), listeTypeDepense.get(i)));
                                }

                                pieEntries.add(new PieEntry((float) sommeTransport, "transport" ));
                                pieEntries.add(new PieEntry((float) sommeNourriture, "nourriture"));
                                pieEntries.add(new PieEntry((float) sommeBesoinHygenique, "besoin hygiénique" ));


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



                    /**
                     * processus pour construire le pieEntries
                     */

                            final GainDao gainDao = new GainDao(context);

                            gainDao.ouvrirBaseDonnee();

                            final double sommeSalaire = gainDao.SommeChamp(salaire);
                            final double sommePret = gainDao.SommeChamp(pret);
                            final double sommeEpargne = gainDao.SommeChamp(epargne);



                            for(int i=0; i < listeTypeGain.size(); i++)
                            {
                                pieEntries.add(new PieEntry((float) gainDao.SommeChamp(listeTypeGain.get(i)), listeTypeGain.get(i)));
                            }

                            pieEntries.add(new PieEntry((float) sommeSalaire, salaire));
                            pieEntries.add(new PieEntry((float) sommePret, pret));
                            pieEntries.add(new PieEntry((float) sommeEpargne, epargne ));


                            gainDao.fermerBaseDeDonnee();

                }


                PieDataSet dataSet = new PieDataSet(pieEntries, titleDiagramme);
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData data = new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.YELLOW);

                diagrammeCirculaire.setData(data);


        return diagrammeCirculaire;
    }

}
