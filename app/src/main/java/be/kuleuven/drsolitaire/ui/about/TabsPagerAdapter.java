/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package be.kuleuven.drsolitaire.ui.about;

//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentPagerAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Adapter for the tabs
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

//    public TabsPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
public TabsPagerAdapter(@NonNull FragmentManager fm) {
    super(fm);
}

//    @Override
//    public androidx.core.app.Fragment getItem(int index) {
//        switch (index) {
//            case 0:
//                return new InformationFragment();
//            case 1:
//                return new LicenseFragment();
//            case 2:
//                return new ChangeLogFragment();
//        }
//
//        return null;
//    }
@NonNull
@Override
public androidx.fragment.app.Fragment getItem(int index) {
    switch (index) {
        case 0:
            return new InformationFragment();
        case 1:
            return new LicenseFragment();
        case 2:
            return new ChangeLogFragment();
    }

    return null;
}
    @Override
    public int getCount() {
        return 3;
    }
}