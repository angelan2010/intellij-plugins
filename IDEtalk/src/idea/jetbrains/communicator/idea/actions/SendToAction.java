/*
 * Copyright 2000-2006 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.communicator.idea.actions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import jetbrains.communicator.commands.SendCodePointerCommand;
import jetbrains.communicator.core.Pico;
import jetbrains.communicator.core.users.User;
import jetbrains.communicator.core.vfs.CodePointer;
import jetbrains.communicator.core.vfs.VFile;
import jetbrains.communicator.idea.VFSUtil;
import jetbrains.communicator.util.StringUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NonNls;

/**
 * @author Kir
 *
 */
public class SendToAction extends BaseEditorPopup {
  @NonNls
  private static final Logger LOG = Logger.getLogger(SendToAction.class);

  protected String getActionDescription(User user, VirtualFile file) {
    return StringUtil.getMsg("code_pointer.description", user.getDisplayName());
  }

  protected void doActionCommand(final User user, final VirtualFile file, final Editor editor) {
    VFile vFile = VFSUtil.createFileFrom(file, editor.getProject());
    if (vFile == null) {
      LOG.info("Unable to send code pointer for " + file);
      return;
    }

    CodePointer codePointer = ActionUtil.getCodePointer(editor);

    SendCodePointerCommand command =
        Pico.getCommandManager().getCommand(SendCodePointerCommand.class, BaseAction.getContainer(editor.getProject()));
    command.setCodePointer(codePointer);
    command.setVFile(vFile);
    command.setUser(user);
    command.execute();
  }

  protected boolean shouldAddUserToChoiceList(User user) {
    return true;
  }

}
