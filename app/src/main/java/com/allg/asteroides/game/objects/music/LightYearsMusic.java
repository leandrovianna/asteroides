package com.allg.asteroides.game.objects.music;

import android.content.Context;

import com.allg.asteroides.R;
import com.allg.asteroides.game.objects.abstracts.Music;

public class LightYearsMusic extends Music {

    public LightYearsMusic(Context context, boolean startAutomatic) {
        super(context, startAutomatic, R.raw.light_years);

        //Source: http://soundimage.org/sci-fi/
    }
}
