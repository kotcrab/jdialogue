/*******************************************************************************
 * DialogueEditor
 * Copyright (C) 2013-2014 Pawel Pastuszak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor.project;

public class PCharacter {
	private int id;
	private String name;
	private String textureName;

	public PCharacter (int id, String name, String textureName) {
		this.name = name;
		this.textureName = textureName;
		this.id = id;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getTextureName () {
		return textureName;
	}

	public int getId () {
		return id;
	}

	public void setTextureName (String textureName) {
		this.textureName = textureName;
	}

	@Override
	public String toString () {
		return name + " (id: " + id + ")";
	}
}
