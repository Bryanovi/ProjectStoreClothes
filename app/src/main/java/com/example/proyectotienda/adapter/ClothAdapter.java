package com.example.proyectotienda.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.proyectotienda.R;
import com.example.proyectotienda.ShowDetailActivity;
import com.example.proyectotienda.domain.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.ViewHolder> {
    ArrayList<ClothDomain> clothDomains;

    // Obtenemos la instancia del objeto FirebaseAuth
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Obtenemos el usuario actualmente autenticado
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // Obtenemos el correo electrónico del usuario
    String userEmail = (currentUser != null) ? currentUser.getEmail() : null;

    // Verificamos si el correo electrónico contiene "%@admin.com"
    boolean isAdmin = userEmail != null && userEmail.contains("@admin.com");


    public ClothAdapter(ArrayList<ClothDomain> clothDomains) {
        this.clothDomains = clothDomains;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothAdapter.ViewHolder holder, int position) {
        holder.title.setText(clothDomains.get(position).getTitle());
        holder.price.setText(String.valueOf(clothDomains.get(position).getPrice()));

        String pic = clothDomains.get(position).getPic();
        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(pic, "drawable", holder.itemView.getContext().getPackageName());

        if (pic.startsWith("http://") || pic.startsWith("https://")) {
            // Cargamos la imagen desde la URL utilizando Glide
            Glide.with(holder.itemView.getContext())
                    .load(pic)
                    .error(R.drawable.no_imagen) // Imagen predeterminada en caso de error
                    .into(holder.pic);
        } else if (drawableResourceId != 0) {
            // Cargamos la imagen desde los recursos locales (drawable)
            Glide.with(holder.itemView.getContext())
                    .load(drawableResourceId)
                    .error(R.drawable.no_imagen) // Imagen predeterminada en caso de error
                    .into(holder.pic);
        } else {
            // Si no se encuentra ni en la URL ni en los recursos locales, cargamos la imagen predeterminada
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.no_imagen)
                    .into(holder.pic);
        }

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("object", clothDomains.get(position));
                intent.putExtra("imageUrl", clothDomains.get(position).getPic()); // Pasamos la URL de la imagen
                holder.itemView.getContext().startActivity(intent);
            }
        });

        // Asignamos el índice de la posición a los botones de editar y eliminar
        holder.editBtn.setTag(position);
        holder.deleteBtn.setTag(position);

        if (isAdmin) {
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                showEditDialog(holder.itemView.getContext(), clickedPosition);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                showDeleteDialog(holder.itemView.getContext(), clickedPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clothDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView pic;
        ImageView addBtn, editBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pic = itemView.findViewById(R.id.pic);
            price = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deletebtn);


        }

    }
    private void showEditDialog(final android.content.Context context, final int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Editar Producto");
        builder.setView(dialogView);

        // Obtenemos las referencias a los campos de edición en el cuadro de diálogo
        EditText editTitle = dialogView.findViewById(R.id.newTitle);
        EditText editDescription = dialogView.findViewById(R.id.newDescription);
        EditText editBrand = dialogView.findViewById(R.id.newBrand);
        EditText editSize = dialogView.findViewById(R.id.newSize);
        EditText editStar = dialogView.findViewById(R.id.newStar);
        EditText editPrice = dialogView.findViewById(R.id.newPrice);
        EditText editPicUrl = dialogView.findViewById(R.id.newPicUrl);

        // Mostramos los valores actuales del elemento en los campos de edición
        ClothDomain cloth = clothDomains.get(position);
        editTitle.setText(cloth.getTitle());
        editDescription.setText(cloth.getDescription());
        editBrand.setText(cloth.getBrand());
        editSize.setText(cloth.getSize());
        editStar.setText(cloth.getStar());
        editPrice.setText(String.valueOf(cloth.getPrice()));
        editPicUrl.setText(cloth.getPic());

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtenemos los nuevos valores ingresados por el usuario
                String newTitle = editTitle.getText().toString();
                String newDescription = editDescription.getText().toString();
                String newBrand = editBrand.getText().toString();
                String newSize = editSize.getText().toString();
                String newStar = editStar.getText().toString();
                String newPic = editPicUrl.getText().toString();
                String newPriceStr = editPrice.getText().toString();

                // Validamos el formato del precio
                if (!isValidPriceFormat(newPriceStr)) {
                    Toast.makeText(context, "Solo números y el punto son permitidos en Price", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validamos el formato de la calificación
                if (!isValidStarFormat(newStar)) {
                    Toast.makeText(context, "Solo Rango de 0 a 5 con un decimal en Star", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newPrice = Double.parseDouble(newPriceStr);

                // Actualizamos el elemento en el ArrayList con los nuevos valores
                ClothDomain updatedCloth = new ClothDomain(newTitle, newPic, newDescription, newStar, newBrand, newSize, newPrice);
                clothDomains.set(position, updatedCloth);
                notifyDataSetChanged();

                Toast.makeText(context, "Producto Actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacemos ninguna acción
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void showDeleteDialog(final android.content.Context context, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar Producto");
        builder.setMessage("¿Estás seguro de eliminar este Producto?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminamos el elemento del ArrayList utilizando el índice almacenado en el botón "deleteBtn"
                clothDomains.remove(position);

                // Notificamos al adaptador sobre el cambio en los datos
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, clothDomains.size());

                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacemos ninguna acción
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean isValidPriceFormat(String price) {
        String pricePattern = "^[0-9]+(\\.[0-9]*)?$"; // Números enteros o con cualquier número de decimales
        return price.matches(pricePattern);
    }

    // Método para validar el formato de la calificación
    private boolean isValidStarFormat(String star) {
        String starPattern = "^(?:5(?:\\.0)?|(?:(?:[0-4](?:\\.[0-9]{1,2})?)|(?:(?:\\.[0-9]{1,2}))))$"; // Números decimales en el rango de 0 a 5
        return star.matches(starPattern);
    }
}
