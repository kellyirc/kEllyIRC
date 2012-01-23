package ui.composites;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;

import shared.ClassFinder;

/**
 * @author Kyle Kemp
 *
 */
public class OptionCompositeContainer extends Composite {

	public static Composite getCurrentComposite() { return currentComposite; }
	
	private static Composite currentComposite;
	private final ScrolledComposite scrolledComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OptionCompositeContainer(Composite parent, int style) {
		super(parent, style);
		
		final Tree tree = new Tree(this, SWT.BORDER);
		tree.setBackground(tree.getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		final GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(tree, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(tree, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
				.add(scrolledComposite, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		setLayout(groupLayout);
		
		//FIXME this needs to work for jar builds!
		TreeItem treeItem;
		try {
			for(Class<?> c : ClassFinder.getClasses("ui.composites")) {
				String name = c.getCanonicalName();
				if(name!=null && name.endsWith("Composite")) {
					treeItem = new TreeItem(tree, SWT.NONE);
					String compname = name.substring(14, name.indexOf("Composite"));
					String[] arrName = compname.split("(?=\\p{Upper})");
					treeItem.setText(Arrays.asList(arrName).toString().replaceAll(",","").replaceAll("^\\[|\\]$", "").trim());
					treeItem.setData(c.getCanonicalName());
				}
			}
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
		treeItem = new TreeItem(tree, SWT.NONE);
		treeItem.setText("Scripting");
		treeItem.setData("ui.composites.ScriptingComposite");
		
		tree.addListener(SWT.MouseDown, new Listener () {
			@Override
			public void handleEvent (Event event) {
				Point point = new Point (event.x, event.y);
				final TreeItem item = tree.getItem (point);

				if (item != null && item.getData()!=null) {
					try {
						Class<?> clazz = Class.forName((String) item.getData());
						Constructor<?> constr = clazz.getConstructor(new Class[] {Composite.class, int.class});
						Composite c = (Composite) constr.newInstance(new Object[] {scrolledComposite, SWT.NONE});
						switchComposite(c);
					} catch (InstantiationException e) {
						e.printStackTrace();
						org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.ui.composites.option");
						fLog.error("Reflection error -- unable to create composite "+item.getData(), e);
					} catch (Exception e) {
						org.apache.log4j.Logger fLog = org.apache.log4j.Logger.getLogger("log.ui.composites.option");
						fLog.error("General error.", e);
					}
				}
			}
		});			

	}

	public void switchComposite(Composite c){
		if(currentComposite!=null){
			currentComposite.dispose();
		}
		currentComposite = c;
		scrolledComposite.setContent(c);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
