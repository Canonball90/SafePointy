package thefellas.safepoint.core.utils.SbarUtils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISearchHandler {

    @CapabilityInject(ISearchHandler.class)
    Capability<ISearchHandler> CAPABILITY = null;

    static boolean hasHandler(ItemStack stack) {
        return stack.hasCapability(CAPABILITY, null);
    }

    static ISearchHandler getHandler(ItemStack stack) {
        return stack.getCapability(CAPABILITY, null);
    }

    /**
     * Determines whether this item's name matches the given query. You should always leave the actual
     * name validation to the <b>matcher</b> parameter, which can be contains, equals, or a regex match,
     * depending on how the query is delimited. This method is only called after the standard validations
     * have been run (item name, enchantments, mod name, etc)<br><br>
     *
     * The <b>search</b> parameter is a function pointer to the method that called this one. This allows
     * for recursive searching, which can be used for say, backpack style items.
     * For example, an item that can contain another item inside (let's call it <i>innerStack</i>), and should
     * respond to queries for that item, would work like this:<br>
     *
     * <pre>return search.namesMatch(innerStack, query);</pre>
     *
     * Given an item which can have an operation mode String (let's call it <i>mode</i>), it would work like this:<br>
     *
     * <pre>return matcher.matches(mode.trim().toLowerCase(), query);</pre>
     *
     * @return false if the item should be darkened, true otherwise
     */
    boolean stackMatchesSearchQuery(String query, ICustomSearchHandler.StringMatcher matcher, ICustomSearchHandler.SearchMethod search);
}
