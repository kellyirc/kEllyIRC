package ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

public class OptionComposite extends Composite {

	private Composite currentComposite;
	private final ScrolledComposite scrolledComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OptionComposite(Composite parent, int style) {
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
		
		TreeItem treeItem = new TreeItem(tree, SWT.NONE);
		treeItem.setText("Connections");
		treeItem.setData("ui.ConnectionComposite");

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
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
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
