/*
 * tvshowtimeplex
 * Copyright (C) 2016  Nuvola - Mrabti Idriss
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.nuvola.tvshowtime.business.plex;

public class Video extends Metadata {

    private Long viewedAt;
    private Long accountID;
    private String parentThumb;
    private Integer deviceID;

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public Long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Long viewedAt) {
        this.viewedAt = viewedAt;
    }


    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getParentThumb() {
        return parentThumb;
    }

    public void setParentThumb(String parentThumb) {
        this.parentThumb = parentThumb;
    }
}
