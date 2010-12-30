/*
 * PinDroid - http://code.google.com/p/PinDroid/
 *
 * Copyright (C) 2010 Matt Schmidt
 *
 * PinDroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * PinDroid is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PinDroid; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package com.pindroid.xml;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.pindroid.providers.BookmarkContent.Bookmark;
import com.pindroid.util.DateParser;

public class SaxBookmarkParser {

	private InputStream is;
	
    public SaxBookmarkParser(InputStream stream) {
    	is = stream;
    }

    public ArrayList<Bookmark> parse() {
        final Bookmark currentBookmark = new Bookmark();
        RootElement root = new RootElement("posts");
        final ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();

        root.getChild("post").setStartElementListener(new StartElementListener(){
            public void start(Attributes attributes) {
            	String url = attributes.getValue("", "href");
            	String time = attributes.getValue("", "time");
            	String description = attributes.getValue("", "description");
            	String extended = attributes.getValue("", "extended");
            	String tag = attributes.getValue("", "tag");
            	String hash = attributes.getValue("", "hash");
            	String meta = attributes.getValue("", "meta");
            	String toread = attributes.getValue("", "toread");
            	String shared = attributes.getValue("", "shared");
            	
            	if(url != null) {
            		currentBookmark.setUrl(url);
            	}
            	if(time != null) {
            		try {
						currentBookmark.setTime(DateParser.parseTime(time));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	if(description != null) {
            		currentBookmark.setDescription(description);
            	}
            	if(extended != null) {
            		currentBookmark.setNotes(extended);
            	}
            	if(tag != null) {
            		currentBookmark.setTagString(tag);
            	}
            	if(hash != null) {
            		currentBookmark.setHash(hash);
            	}
            	if(meta != null) {
            		currentBookmark.setMeta(meta);
            	}

            	currentBookmark.setToRead(toread != null && toread.equals("yes"));

            	currentBookmark.setShared(!(shared != null && shared.equals("no")));

            	
            	bookmarks.add(currentBookmark.copy());
            }
        });
        try {
            Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bookmarks;
    }
}