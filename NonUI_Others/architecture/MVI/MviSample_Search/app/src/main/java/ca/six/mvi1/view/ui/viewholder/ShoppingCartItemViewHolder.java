/*
 * Copyright 2017 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.six.mvi1.view.ui.viewholder;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.six.mvi1.R;
import ca.six.mvi1.businesslogic.model.Product;
import ca.six.mvi1.dependencyinjection.DependencyInjection;
import ca.six.mvi1.view.shoppingcartoverview.ShoppingCartOverviewItem;

/**
 * @author Hannes Dorfmann
 */

public class ShoppingCartItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemSelectedListener selectedListener;
    private final Drawable selectedDrawable;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.price)
    TextView price;
    private ShoppingCartOverviewItem item;
    private ShoppingCartItemViewHolder(View itemView, ItemSelectedListener itemSelectedListener) {
        super(itemView);
        this.selectedListener = itemSelectedListener;
        itemView.setOnClickListener(v -> selectedListener.onItemClicked(item));
        itemView.setOnLongClickListener(v -> selectedListener.onItemLongPressed(item));
        selectedDrawable = new ColorDrawable(
                itemView.getContext().getResources().getColor(R.color.selected_shopping_cart_item));

        ButterKnife.bind(this, itemView);
    }

    public static ShoppingCartItemViewHolder create(LayoutInflater inflater,
                                                    ItemSelectedListener selectedListener) {
        return new ShoppingCartItemViewHolder(
                inflater.inflate(R.layout.item_shopping_cart, null, false), selectedListener);
    }

    public void bind(ShoppingCartOverviewItem item) {
        this.item = item;
        Product product = item.getProduct();

        Glide.with(itemView.getContext())
                .load(DependencyInjection.BASE_IMAGE_URL + product.getImage())
                .centerCrop()
                .into(image);

        name.setText(product.getName());
        price.setText(String.format(Locale.US, "$ %.2f", product.getPrice()));

        if (item.isSelected()) {
            if (Build.VERSION.SDK_INT >= 23) {
                itemView.setForeground(selectedDrawable);
            } else {
                itemView.setBackground(selectedDrawable);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                itemView.setForeground(null);
            } else {
                itemView.setBackground(null);
            }
        }
    }

    public interface ItemSelectedListener {
        public void onItemClicked(ShoppingCartOverviewItem product);

        public boolean onItemLongPressed(ShoppingCartOverviewItem product);
    }
}
