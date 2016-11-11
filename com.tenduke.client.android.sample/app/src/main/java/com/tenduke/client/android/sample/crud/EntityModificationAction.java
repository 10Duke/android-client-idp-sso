package com.tenduke.client.android.sample.crud;

import android.content.Intent;
import android.support.annotation.Nullable;

/** Actions describing the the possible entity modifications.
 *
 */
public enum EntityModificationAction {

    /** Entity created. */
    CREATE(2),
    /** Entity deleted. */
    DELETE(3),
    /** Entity updated. */
    UPDATE(4);

    private final int _activityResultCode;


    /** Constructor
     *
     * @param activityResultCode Result code to be returned by an activity via {@link android.app.Activity#setResult(int, Intent)}.
     */
    EntityModificationAction(int activityResultCode) {
        _activityResultCode = activityResultCode;
    }


    /** Returns the corresponding activity result code, to be used when calling {@link android.app.Activity#setResult(int, Intent)}.
     *
     * @return the activity result code
     */
    public int getActivityResultCode() {
        return _activityResultCode;
    }


    /** Maps the result code into EntityModificationAction.
     *
     * @param activityResultCode the result code to map
     * @return the corresponding EntityModificationAction. Returns {@code null} if unknown result code.
     */
    public static EntityModificationAction byResultCode(int activityResultCode) {
        //
        for (final EntityModificationAction action : values()) {
            if (action._activityResultCode == activityResultCode) {
                return action;
            }
        }
        return null;
    }


    /** Maps the entity modification action based on previous action.
     *
     *  <p>This is used for following, assume the user is on "entity detail"-activity:</p>
     *  <ol>
     *      <li>The user creates an entity and then deletes the same entity: No action, e.g. The
     *          "entity list"-view does not need to be modified in anyway.
     *      </li>
     *      <li>The user modifies an existing entity and then deletes the same entity: Delete, e.g.
     *          the "entity list"-view should remove the entity.
     *      </li>
     *  </ol>
     *
     * @param _previousAction previous action on the entity while user was in this same view
     * @return the EntityModificationAction, which should be returned.
     */
    public @Nullable EntityModificationAction mapToResultingAction (@Nullable final EntityModificationAction _previousAction) {
        //
        if (_previousAction == null) {
            return this;
        }
        if (_previousAction == CREATE) {
            if (this == DELETE) {
                return null;
            }
            return _previousAction;
        }
        return this;
    }
}
