package com.feed_the_beast.mods.ftbguilibrary.config.gui;

import com.feed_the_beast.mods.ftbguilibrary.config.ConfigCallback;
import com.feed_the_beast.mods.ftbguilibrary.config.ConfigFluid;
import com.feed_the_beast.mods.ftbguilibrary.icon.Color4I;
import com.feed_the_beast.mods.ftbguilibrary.icon.Icon;
import com.feed_the_beast.mods.ftbguilibrary.icon.ItemIcon;
import com.feed_the_beast.mods.ftbguilibrary.misc.GuiButtonListBase;
import com.feed_the_beast.mods.ftbguilibrary.utils.Key;
import com.feed_the_beast.mods.ftbguilibrary.utils.MouseButton;
import com.feed_the_beast.mods.ftbguilibrary.widget.Panel;
import com.feed_the_beast.mods.ftbguilibrary.widget.SimpleTextButton;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class GuiSelectFluid extends GuiButtonListBase
{
	private final ConfigFluid config;
	private final ConfigCallback callback;

	public GuiSelectFluid(ConfigFluid c, ConfigCallback cb)
	{
		setTitle(new TranslationTextComponent("ftbguilibrary.select_fluid.gui"));
		setHasSearchBox(true);
		config = c;
		callback = cb;
	}

	@Override
	public void addButtons(Panel panel)
	{
		if (config.allowEmpty)
		{
			FluidStack fluidStack = new FluidStack(Fluids.EMPTY, FluidAttributes.BUCKET_VOLUME);

			panel.add(new SimpleTextButton(panel, fluidStack.getDisplayName(), ItemIcon.getItemIcon(Items.BUCKET))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					playClickSound();
					config.setCurrentValue(Fluids.EMPTY);
					callback.save(true);
				}

				@Override
				public Object getIngredientUnderMouse()
				{
					return new FluidStack(Fluids.EMPTY, FluidAttributes.BUCKET_VOLUME);
				}
			});
		}

		for (Fluid fluid : ForgeRegistries.FLUIDS)
		{
			if (fluid == Fluids.EMPTY || fluid.getDefaultState().isSource())
			{
				continue;
			}

			FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);
			FluidAttributes attributes = fluidStack.getFluid().getAttributes();

			panel.add(new SimpleTextButton(panel, fluidStack.getDisplayName(), Icon.getIcon(attributes.getStillTexture(fluidStack)).withTint(Color4I.rgb(attributes.getColor(fluidStack))))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					playClickSound();
					config.setCurrentValue(fluid);
					callback.save(true);
				}

				@Override
				public Object getIngredientUnderMouse()
				{
					return new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);
				}
			});
		}
	}

	@Override
	public boolean onClosedByKey(Key key)
	{
		if (super.onClosedByKey(key))
		{
			callback.save(false);
			return false;
		}

		return false;
	}
}