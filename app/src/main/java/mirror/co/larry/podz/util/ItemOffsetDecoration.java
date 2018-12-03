package mirror.co.larry.podz.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    int space;
    public ItemOffsetDecoration(Context context, int spacing){
        space = context.getResources().getDimensionPixelSize(spacing);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)==0){
            outRect.top = space;
            outRect.bottom=space;
        }else{
            outRect.bottom= space;
        }
        outRect.left = space;
        outRect.right = space;
    }
}
