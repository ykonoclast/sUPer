package org.duckdns.spacedock.sUPer.presentation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import org.duckdns.spacedock.sUPer.R;

/**
 * Fragment représentant une boite de dialogue permettant de modifier le statut d'un combatant
 */
public class STATDialogFragment extends DialogFragment
{//TODO implémenter meilleure méthode de communication avec l'activité via une interface que l'activité implémentera et qui contiendra une méthode de callback. Actuellement dépendant de la classe spécifique de l'activité
    //TODO extraire une superclasse pour les fragments de cette application qui sont très proches les uns des autres
    private FightBoard m_activity;
    private int m_index;
    private EditText m_NDEditText;

    /**
     * listener du bouton OK de la boite de dialogue, appelle la métode de callback associée dans l'activité principale
     */
    private final DialogInterface.OnClickListener m_STATDialogListener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int id)
        {
            //TODO: la ligne suivante bien que d'une concision admirable fait trop à la fois, la diviser pour plus de clarté
            m_activity.STATChangedCallback(m_index, Integer.parseInt(m_NDEditText.getText().toString()));//on passe à l'activité le nouveau ND cible
        }
    };

    /**
     * Véritable "constructeur" fournissant l'instance unique mais SURTOUT recevant des paramétres et permettant donc leur affectation à des variable membres dans OnCreateDialog()
     *
     * @param p_index
     * @return
     */
    static STATDialogFragment getInstance(int p_index, int p_ND)
    {
        STATDialogFragment frag = new STATDialogFragment();
        Bundle args = new Bundle();
        args.putInt("index", p_index);
        args.putInt("nd", p_ND);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        m_activity = (FightBoard) getActivity();
        m_index = getArguments().getInt("index");

        //crée un builder pour construire la boite de dialogue et lui passe l'activité mère comme contexte avant de lui affecter un titre
        AlertDialog.Builder builder = new AlertDialog.Builder(m_activity);
        builder.setTitle(R.string.STATDialogTitle);

        //inflate la vue de la boite de dialogue à partir du layout xml et l'affecte au builder
        View statSetupView = m_activity.getLayoutInflater().inflate(R.layout.fragment_stat_dialog, null);//TODO: voir si il est possible de passer autre chose que null comme rootelement
        builder.setView(statSetupView);

        //récupère des pointeurs sur les widgets d'interraction présents dans la boite dialogue
        m_NDEditText = (EditText) statSetupView.findViewById(R.id.targetNDValue);

        //configuration initiale des champs
        m_NDEditText.setText("" + getArguments().getInt("nd"));

        builder.setCancelable(true);//ainsi on pourra faire back pour annuler

        //Passe au bouton positif son listener, le négatif est géré par le fait qu'il n'y a simplement rien à faire en ce cas
        builder.setPositiveButton(R.string.DialogModify, m_STATDialogListener);
        builder.setNegativeButton(R.string.DialogCancelButton, null);

        //la méthode create produit la boite mais ne l'afiche pas, ce sera l'activité qui appellera show() sur ce fragment
        return (builder.create());
    }
}
