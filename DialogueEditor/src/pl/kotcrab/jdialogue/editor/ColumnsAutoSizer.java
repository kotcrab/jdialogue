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

package pl.kotcrab.jdialogue.editor;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.FontMetrics;

//FROM: http://bosmeeuw.wordpress.com/2011/08/07/java-swing-automatically-resize-table-columns-to-their-contents/
//Modified version

public class ColumnsAutoSizer {
	public static void sizeColumnsToFit (JTable table) {
		sizeColumnsToFit(table, 80);
	}

	public static void sizeColumnsToFit (JTable table, int columnMargin) {
		JTableHeader tableHeader = table.getTableHeader();

		if (tableHeader == null) {
			// can't auto size a table without a header
			return;
		}

		FontMetrics headerFontMetrics = tableHeader.getFontMetrics(tableHeader.getFont());

		int[] minWidths = new int[table.getColumnCount()];
		int[] maxWidths = new int[table.getColumnCount()];

		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			int headerWidth = headerFontMetrics.stringWidth(table.getColumnName(columnIndex));

			minWidths[columnIndex] = headerWidth + columnMargin;

			int maxWidth = getMaximalRequiredColumnWidth(table, columnIndex, headerWidth);

			maxWidths[columnIndex] = Math.max(maxWidth, minWidths[columnIndex]) + columnMargin;
		}

		adjustMaximumWidths(table, minWidths, maxWidths);

		for (int i = 0; i < 1; i++) // !!! We are adjusting ONLY one first column!
		{
			if (minWidths[i] > 0) {
				table.getColumnModel().getColumn(i).setMinWidth(minWidths[i]);
			}

			if (maxWidths[i] > 0) {
				table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);

				table.getColumnModel().getColumn(i).setWidth(maxWidths[i]);
			}
		}
	}

	private static void adjustMaximumWidths (JTable table, int[] minWidths, int[] maxWidths) {
		if (table.getWidth() > 0) {
			// to prevent infinite loops in exceptional situations
			int breaker = 0;

			// keep stealing one pixel of the maximum width of the highest column until we can fit in the width of the table
			while (sum(maxWidths) > table.getWidth() && breaker < 10000) {
				int highestWidthIndex = findLargestIndex(maxWidths);

				maxWidths[highestWidthIndex] -= 1;

				maxWidths[highestWidthIndex] = Math.max(maxWidths[highestWidthIndex], minWidths[highestWidthIndex]);

				breaker++;
			}
		}
	}

	private static int getMaximalRequiredColumnWidth (JTable table, int columnIndex, int headerWidth) {
		int maxWidth = headerWidth;

		TableColumn column = table.getColumnModel().getColumn(columnIndex);

		TableCellRenderer cellRenderer = column.getCellRenderer();

		if (cellRenderer == null) {
			cellRenderer = new DefaultTableCellRenderer();
		}

		for (int row = 0; row < table.getModel().getRowCount(); row++) {
			Component rendererComponent = cellRenderer.getTableCellRendererComponent(table, table.getModel().getValueAt(row, columnIndex), false, false, row, columnIndex);

			double valueWidth = rendererComponent.getPreferredSize().getWidth();

			maxWidth = (int) Math.max(maxWidth, valueWidth);
		}

		return maxWidth;
	}

	private static int findLargestIndex (int[] widths) {
		int largestIndex = 0;
		int largestValue = 0;

		for (int i = 0; i < widths.length; i++) {
			if (widths[i] > largestValue) {
				largestIndex = i;
				largestValue = widths[i];
			}
		}

		return largestIndex;
	}

	private static int sum (int[] widths) {
		int sum = 0;

		for (int width : widths) {
			sum += width;
		}

		return sum;
	}
}
