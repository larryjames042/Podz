package mirror.co.larry.podz.Util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecorationGenre extends RecyclerView.ItemDecoration {
    int space;
    public ItemOffsetDecorationGenre(Context context, int spacing){
        space = context.getResources().getDimensionPixelSize(spacing);
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)==0){
            outRect.left = space;
            outRect.right = space;
        }else{
            outRect.right= space;
        }
        outRect.top = space;
        outRect.bottom = space;
    }
}
