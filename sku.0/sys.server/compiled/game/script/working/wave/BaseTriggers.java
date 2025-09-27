package script.working.wave;

import script.menu_info;
import script.obj_id;
import script.string_id;

public final class BaseTriggers
{
    public interface Base
    {
        int OnAttach(obj_id self);

        int OnDetach(obj_id self);

        int OnInitialize(obj_id self);

        int OnDestroy(obj_id self);
    }

    public interface Listen
    {
        int OnSpeaking(obj_id self, String text);

        int OnHearSpeech(obj_id self, obj_id speaker, String text);
    }

    public interface Menu
    {
        int OnObjectMenuSelect(obj_id self, obj_id player, int item);

        int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item);
    }

    public interface TriggerVolume
    {
        int OnTriggerVolumeEntered(obj_id self, String name, obj_id who);

        int OnTriggerVolumeExited(obj_id self, String name, obj_id who);
    }

    public interface Combat
    {
        int OnEnteredCombat(obj_id self);

        int OnExitedCombat(obj_id self);
    }

    public interface Region
    {
        int OnEnterRegion(obj_id self, String regionName, obj_id who);

        int OnExitRegion(obj_id self, String regionName, obj_id who);
    }

    public interface Creature
    {
        int OnCreatureDamaged(obj_id self, obj_id attacker, obj_id weapon, int[] damage);
    }

    public interface Objects
    {
        int OnObjectDisabled(obj_id self, obj_id killer);

        int OnObjectDamaged(obj_id self, obj_id attacker, obj_id weapon, int[] damage);
    }

    public interface Death
    {
        int OnDeath(obj_id self, obj_id killer, obj_id corpseId);

        int OnIncapacitated(obj_id self, obj_id killer);
    }

    public interface Mob
    {
        int OnMovePathComplete(obj_id self);

        int OnMovePathNotFound(obj_id self);
    }

    public interface Conversation
    {
        int OnStartNpcConversation(obj_id self, obj_id speaker);

        int OnNpcConversationResponse(obj_id self, String convo, obj_id player, string_id response);
    }

    public interface CombatAction
    {
        int OnDefenderCombatAction(obj_id self, obj_id attacker, obj_id weapon, int combatResult);
    }

    public interface Player
    {
        int OnDeath(obj_id self, obj_id killer, obj_id corpseId);

        int OnIncapacitated(obj_id self, obj_id killer);

        int OnEnteredCombat(obj_id self);

        int OnExitedCombat(obj_id self);

        int OnSpeaking(obj_id self, String text);

        int OnHearSpeech(obj_id self, obj_id speaker, String text);

        int OnCSCreateItem(obj_id self, obj_id player, String template);

        int OnGroundTargetLoc(obj_id self, obj_id player, int menuItem, float x, float y, float z);

    }

    public interface Group
    {
        int OnRemovedFromGroup(obj_id self, obj_id groupId);

        int OnAddedToGroup(obj_id self, obj_id groupId);

        int OnGroupLeaderChanged(obj_id self, obj_id groupId);

        int OnGroupDisbanded(obj_id self, obj_id groupId);

        int OnGroupFormed(obj_id self, obj_id groupId);
    }
}