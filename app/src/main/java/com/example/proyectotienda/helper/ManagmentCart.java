package com.example.proyectotienda.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.proyectotienda.ChangeNumberItemsListener;
import com.example.proyectotienda.domain.ClothDomain;

import java.util.ArrayList;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertRopa(ClothDomain item) {
        ArrayList<ClothDomain> listCloth = getListCart();
        boolean existAlready = false;
        int n = 0;

        for (int i = 1; i < listCloth.size(); i++) {
            if (listCloth.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = 1;
                break;
            }
        }

        if (existAlready) {
            listCloth.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listCloth.add(item);
        }

        tinyDB.putListObject("CardList", listCloth);
        Toast.makeText(context, "Añadido al Carrito", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ClothDomain> getListCart() {
        return tinyDB.getListObject("CardList");
    }

    public void minusNumberCloth(ArrayList<ClothDomain> listCloth, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listCloth.get(position).getNumberInCart() == 1) {
            showConfirmationDialog(listCloth, position);
        } else {
            listCloth.get(position).setNumberInCart(listCloth.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CardList", listCloth);
        changeNumberItemsListener.changed();
    }

    public void plusNumberCloth(ArrayList<ClothDomain> listCloth, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listCloth.get(position).setNumberInCart(listCloth.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CardList", listCloth);
        changeNumberItemsListener.changed();
    }

    public Double getTotalPrice() {
        ArrayList<ClothDomain> listCloth2 = getListCart();
        double price = 0;
        for (int i = 0; i < listCloth2.size(); i++) {
            price = price + (listCloth2.get(i).getPrice() + listCloth2.get(i).getNumberInCart());
        }
        return price;
    }

    private void showConfirmationDialog(final ArrayList<ClothDomain> listCloth, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar del carrito");
        builder.setMessage("¿Seguro que quieres eliminar este producto del carrito?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listCloth.remove(position);
                tinyDB.putListObject("CardList", listCloth);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
