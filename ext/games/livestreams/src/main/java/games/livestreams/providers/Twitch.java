package games.livestreams.providers;

import games.livestreams.ExtensionObject;
import games.livestreams.errors.ProviderError;
import games.livestreams.Provider;

import org.jaibo.api.IrcMessageTextModifier;
import org.jaibo.api.IrcTextColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Twitch.tv live streams extension provider
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
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

public class Twitch extends Provider {


    @Override
    public String[] getStreams(String tag) {
        final ArrayList<String> streams = new ArrayList<String>();
        String apiUrl = String.format(
                ExtensionObject.Configuration.get("twitch.link"),
                Integer.parseInt(ExtensionObject.Configuration.get("streams.limit")),
                tag);

        try {
//            apiUrl = URLEncoder.encode(apiUrl, "UTF-8");
            URL url = new URL(apiUrl);

            Scanner scan = new Scanner(url.openStream());
            String jsonAnswer = "";
            while (scan.hasNext())
                jsonAnswer += scan.nextLine();
            scan.close();

            JSONObject jsonObject = new JSONObject(jsonAnswer);
            JSONArray jsonArray = jsonObject.getJSONArray("streams");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject streamObject = jsonArray.getJSONObject(i);

                String streamName = streamObject.getJSONObject("channel").getString("display_name");
                String streamStatus = "online";
                String streamUrl = streamObject.getJSONObject("channel").getString("url");
                String streamViewers = streamObject.getString("viewers");

                streamName = IrcMessageTextModifier.makeBold(streamName);
                streamViewers = IrcMessageTextModifier.makeColoured(IrcMessageTextModifier.makeBold(streamViewers),
                        IrcTextColor.Brown);

                String realStatus = streamObject.getJSONObject("channel").getString("status");

                if (realStatus != null && !realStatus.trim().isEmpty()) {
                    streamStatus = realStatus;
                }

                String formattedStreamInfoOutput = String.format("[%s] (%s) %s (%s viewers)",
                        streamName, streamStatus, streamUrl, streamViewers);

                streams.add(formattedStreamInfoOutput);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!streams.isEmpty()) {
            String[] streamsArray = new String[]{};

            return streams.toArray(streamsArray);
        } else {
            throw new ProviderError(String.format("No streams found on \"%s\" service with tag \"%s\"",
                    this.getProviderName(), tag));
        }
    }

    @Override
    public String getProviderName() {
        return "Twitch";
    }
}