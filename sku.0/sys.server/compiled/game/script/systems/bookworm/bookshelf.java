package script.systems.bookworm;/*
@Origin: dsrc.script.systems.bookworm
@Author: BubbaJoeX
@Purpose: Object to purchase books from.
@Note: Make sure the object this is attached to has a GOT of got_terminal_misc.
@Created: Sunday, 10/1/2023, at 11:45 AM, 
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.*;
import script.library.money;
import script.library.sui;
import script.library.utils;

public class bookshelf extends base_script
{
    public static String BOOK_DATATABLE = "datatables/adhoc/bookshelf.iff";
    public static int BOOK_COST = 2000;

    public int OnAttach(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self)
    {
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, unlocalized("Purchase Books"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int menu) throws InterruptedException
    {
        if (menu == menu_info_types.ITEM_USE)
        {
            openBookshelf(self, player);
        }
        return SCRIPT_CONTINUE;
    }


    public int openBookshelf(obj_id self, obj_id player) throws InterruptedException
    {
        String[] books = dataTableGetStringColumn(BOOK_DATATABLE, "book_type");
        sui.listbox(self, player, "Select a book to purchase.\n\tAll books cost 2,000 credits each.", sui.OK_CANCEL, "Bookshelf", books, "handleBookshelfPurchase");
        return SCRIPT_CONTINUE;
    }

    public int handleBookshelfPurchase(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        int row = sui.getListboxSelectedRow(params);
        dictionary selectedBook = dataTableGetRow(BOOK_DATATABLE, row);
        String bookTemplate = selectedBook.getString("book_template");
        int bookCost = selectedBook.getInt("book_cost");
        if (bookTemplate != null && !bookTemplate.equals(""))
        {
            if (isIdValid(utils.getInventoryContainer(player)))
            {
                obj_id book = createObject(bookTemplate, utils.getInventoryContainer(player), "");
                if (money.requestPayment(player, self, bookCost, "pass_fail", params, false))
                {
                    LOG("ethereal", "[Bookshelf]: Created book " + bookTemplate + " for player " + getPlayerFullName(player) + " for " + bookCost + " credits.");
                    broadcast(player, "You have purchased a book for " + bookCost + "  credits.");
                    if (!hasScript(book, "systems.bookworm.book"))
                    {
                        attachScript(book, "systems.bookworm.book");
                    }
                    setName(book, "a writable object");
                    setDescriptionString(book, "This is a writable object. You can write in it by using the radial menu if you are it's owner.");
                    setCrafter(book, player);
                }
                else
                {
                    LOG("ethereal", "[Bookshelf]: Failed to create book " + bookTemplate + " for player " + getPlayerFullName(player) + " for " + bookCost + " credits due to insufficient funds.");
                    broadcast(player, "You do not have enough credits to purchase this book.");
                    destroyObject(book);
                }
            }
            else
            {
                LOG("ethereal", "[Bookshelf]: Failed to create book " + bookTemplate + " for player " + getPlayerFullName(player));
                broadcast(player, "There was an error purchasing this book. Please try again later.");
            }
        }
        return SCRIPT_CONTINUE;
    }

    private string_id unlocalized(String string)
    {
        return new string_id(string);
    }
}
