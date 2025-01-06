package com.example.proyectotienda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectotienda.domain.ClothDomain;
import com.example.proyectotienda.helper.ManagmentCart;



public class ShowDetailActivity extends AppCompatActivity {
    private TextView addToCartBtn;
    private TextView titleTxt;
    private TextView feeTxt;
    private TextView descriptionTxt, totalPriceTxt, numberOrderTxt, starTxt, sizeTxt,brandTxt;
    private ImageView plusBtn;
    private ImageView minusBtn;
    private ImageView picCloth;
    private ClothDomain object;

    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        managmentCart = new ManagmentCart(this);
        initView();
        getBundle();

    }

    private void getBundle() {
        object = (ClothDomain) getIntent().getSerializableExtra("object");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (object != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.no_imagen) // Imagen de respaldo mientras carga la imagen desde la URL.
                    .error(R.drawable.no_imagen) // Imagen de respaldo en caso de error al cargar la imagen desde la URL.
                    .into(picCloth);
        } else {
            picCloth.setImageResource(R.drawable.no_imagen); // Si no hay objeto, muestra la imagen de respaldo directamente.
        }

        titleTxt.setText(object.getTitle());
        feeTxt.setText("$"+object.getPrice());
        descriptionTxt.setText(object.getDescription());
        numberOrderTxt.setText(String.valueOf(numberOrder));
        sizeTxt.setText(object.getSize());
        starTxt.setText(object.getStar());
        brandTxt.setText(object.getBrand());
        totalPriceTxt.setText("$"+Math.round(numberOrder * object.getPrice()));

        plusBtn.setOnClickListener(v -> {
            numberOrder = numberOrder+1;
            numberOrderTxt.setText(String.valueOf(numberOrder));
            totalPriceTxt.setText("$"+Math.round(numberOrder * object.getPrice()));

        });

        minusBtn.setOnClickListener(v -> {
            if (numberOrder>1){
                numberOrder = numberOrder-1;
            }
            numberOrderTxt.setText(String.valueOf(numberOrder));
            totalPriceTxt.setText("$"+Math.round(numberOrder * object.getPrice()));

        });

        addToCartBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managmentCart.insertRopa(object);
        });

    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        feeTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        numberOrderTxt = findViewById(R.id.numberOrderTxt);
        //numberOrder = findViewById(R.id.numberOrderTxt);
        plusBtn = findViewById(R.id.plusCardBtn);
        minusBtn = findViewById(R.id.minusCardBtn);
        picCloth = findViewById(R.id.clothPic);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        starTxt = findViewById(R.id.starTxt);
        sizeTxt = findViewById(R.id.sizeTxt);
        brandTxt = findViewById(R.id.brandTxt);


    }

    private void loadPicWithGlide(String imageUrl, ImageView imageView) {
        if (imageUrl != null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
            // Cargamos la imagen desde la URL utilizando Glide
            Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Almacenamos en caché
                    .error(R.drawable.no_imagen) // Imagen predeterminada en caso de error o URL inválida
                    .into(imageView);
        } else {
            // Si la URL no es válida o no comienza con "http://" o "https://",
            // entonces cargamos la imagen desde los recursos locales (drawable)
            int drawableResourceId = this.getResources().getIdentifier(object.getPic(), "drawable", this.getPackageName());
            Glide.with(this)
                    .load(drawableResourceId)
                    .error(R.drawable.no_imagen) // Imagen predeterminada en caso de error o URL inválida
                    .into(imageView);
        }
    }




}
