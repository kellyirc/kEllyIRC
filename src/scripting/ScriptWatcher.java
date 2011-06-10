package scripting;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import static java.nio.file.StandardWatchEventKinds.*;

public class ScriptWatcher implements Runnable{

	private WatchService watcher;
	private Map<WatchKey, Path> keys;

	public ScriptWatcher() {
		File f = new File("./scripts/");
		if(!f.isDirectory()) { f.mkdirs(); }
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.keys = new HashMap<WatchKey, Path>();
			register(Paths.get("./scripts/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		keys.put(key, dir);

		for (File f : dir.toFile().listFiles()) {
			if (f.isDirectory() || !f.toString().contains(".js"))
				continue;
			ScriptManager.addScript(f);
		}
	}

	void updateFile(WatchEvent<?> event, File file) {
		String eventType = event.kind().name();
		if (eventType.equals("ENTRY_MODIFY")) {
			ScriptManager.modifyScript(file);

		} else if (eventType.equals("ENTRY_ADD")) {
			ScriptManager.addScript(file);

		} else if (eventType.equals("ENTRY_DELETE")) {
			ScriptManager.removeScript(file);

		}
	}

	@Override
	public void run() {
		while (true) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				x.printStackTrace();
				return;
			}

			Path dir = keys.get(key);

			if (dir == null) {
				System.err.println("Unrecognized key: " + key);
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {

				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path name = ev.context();
				Path child = dir.resolve(name);
				File f = child.toFile();
				updateFile(event, f);
			}
		}
	}
}