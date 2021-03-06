package com.feed_the_beast.mods.ftbguilibrary.widget;

import com.feed_the_beast.mods.ftbguilibrary.icon.Color4I;
import com.feed_the_beast.mods.ftbguilibrary.icon.Icon;
import com.feed_the_beast.mods.ftbguilibrary.utils.Bits;
import com.feed_the_beast.mods.ftbguilibrary.utils.StringUtils;
import com.feed_the_beast.mods.ftbguilibrary.utils.TooltipList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author LatvianModder
 */
public class TextField extends Widget
{
	public String[] text;
	public int textFlags = 0;
	public int maxWidth = 0;
	public int textSpacing = 10;
	public float scale = 1F;
	public Color4I textColor = Icon.EMPTY;

	public TextField(Panel panel)
	{
		super(panel);
	}

	public TextField addFlags(int flags)
	{
		textFlags |= flags;
		return this;
	}

	public TextField setMaxWidth(int width)
	{
		maxWidth = width;
		return this;
	}

	public TextField setColor(Color4I color)
	{
		textColor = color;
		return this;
	}

	public TextField setScale(float s)
	{
		scale = s;
		return this;
	}

	public TextField setSpacing(int s)
	{
		textSpacing = s;
		return this;
	}

	public TextField setText(String txt)
	{
		text = null;
		txt = txt.trim();
		Theme theme = getGui().getTheme();

		if (!txt.isEmpty())
		{
			if (maxWidth == 0)
			{
				text = txt.split("\n");
			}
			else
			{
				text = theme.listFormattedStringToWidth(new StringTextComponent(txt), maxWidth)
						.stream()
						.map(ITextProperties::getString)
						.toArray(String[]::new);
			}
		}

		if (text == null || text.length == 0)
		{
			text = StringUtils.EMPTY_ARRAY;
		}

		return resize(theme);
	}

	public TextField resize(Theme theme)
	{
		if (maxWidth == 0)
		{
			setWidth(0);

			for (String s : text)
			{
				setWidth(Math.max(width, (int) (theme.getStringWidth(s) * scale)));
			}
		}
		else
		{
			setWidth(maxWidth);
		}

		setHeight((int) ((Math.max(1, text.length) * textSpacing - (textSpacing - theme.getFontHeight() + 1)) * scale));
		return this;
	}

	@Override
	public void addMouseOverText(TooltipList list)
	{
	}

	public void drawBackground(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h)
	{
	}

	@Override
	public void draw(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h)
	{
		drawBackground(matrixStack, theme, x, y, w, h);

		if (text.length == 0)
		{
			return;
		}

		boolean centered = Bits.getFlag(textFlags, Theme.CENTERED);
		boolean centeredV = Bits.getFlag(textFlags, Theme.CENTERED_V);

		Color4I col = textColor;

		if (col.isEmpty())
		{
			col = theme.getContentColor(WidgetType.mouseOver(Bits.getFlag(textFlags, Theme.MOUSE_OVER)));
		}

		int tx = x + (centered ? (w / 2) : 0);
		int ty = y + (centeredV ? ((h - theme.getFontHeight()) / 2) : 0);

		if (scale == 1F)
		{
			for (int i = 0; i < text.length; i++)
			{
				theme.drawString(matrixStack, text[i], tx, ty + i * textSpacing, col, textFlags);
			}
		}
		else
		{
			matrixStack.push();
			matrixStack.translate(tx, ty, 0);
			matrixStack.scale(scale, scale, 1F);

			for (int i = 0; i < text.length; i++)
			{
				theme.drawString(matrixStack, text[i], 0, i * textSpacing, col, textFlags);
			}

			matrixStack.pop();
		}
	}
}
