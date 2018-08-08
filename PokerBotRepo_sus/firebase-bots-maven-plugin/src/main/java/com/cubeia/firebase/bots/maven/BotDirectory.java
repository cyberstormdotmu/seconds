/**
 * Copyright 2009 Cubeia Ltd 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cubeia.firebase.bots.maven;

import java.io.File;

public class BotDirectory {

	public final File botDirectory;
	public final File libDirectory;
	public final File confDirectory;

	public BotDirectory(String version, File botDir) {
		this.botDirectory = botDir;
		libDirectory = new File(botDir, "lib");
		confDirectory = new File(botDir, "conf");
	}
}
