package com.psygate.xpylons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.geometry.AABox;

public class Configuration {
	private List<XPBlock> template = new LinkedList<XPBlock>();
	private Material activationMaterial;
	private AABox pylonBox;
	private int minPylonDistance;
	private float growthStopChance;
	private double xpPerGrowth;
	private DBLayer dblayer;

	public Configuration(XPylons pylons) {
		FileConfiguration conf = pylons.getConfig();
		conf.options().copyDefaults(true);
		pylons.saveConfig();

		for (Object s : conf.getList("template")) {
			String q = (String) s;
			String[] d = q.split(" ");
			int x = Integer.parseInt(d[0]);
			int y = Integer.parseInt(d[1]);
			int z = Integer.parseInt(d[2]);
			Material mat = Material.getMaterial(d[3]);
			template.add(new XPBlock(x, y, z, mat));
		}

		activationMaterial = Material.getMaterial(conf.getString("activationMaterial"));
		String[] box = conf.getString("pylonBox").split(" ");
		pylonBox = new AABox(Integer.parseInt(box[0]), Integer.parseInt(box[1]), Integer.parseInt(box[2]));
		minPylonDistance = conf.getInt("minPylonDistance");
		growthStopChance = (float) Float.parseFloat(conf.getString("growthStopChance").replace("%", "")) / 100;
		xpPerGrowth = conf.getDouble("xpPerGrowth");
		dblayer = new PassThroughLayer();
	}

	public boolean isPylon(Block checkbase) {
		Location base = checkbase.getLocation();
		World w = checkbase.getWorld();

		for (XPBlock should : template) {
			Location truelocation = new Location(w, base.getX() + should.getX(), base.getY() + should.getY(),
					base.getZ() + should.getZ());
			Material mat = Material.getMaterial(should.getMaterial());
			if (!(w.getBlockAt(truelocation).getType() == mat)) {
				return false;
			}
		}

		return true;
	}

	public List<XPBlock> getPylonBlockTemplate() {
		return template;
	}

	public Material getActivationMaterial() {
		return activationMaterial;
	}

	public AABox getPylonBox() {
		return pylonBox;
	}

	public int getMinPylonDistance() {
		return minPylonDistance;
	}

	public float getGrowthStopChance() {
		return growthStopChance;
	}

	public double getXpPerGrowth() {
		return xpPerGrowth;
	}

	public List<XPBlock> getTemplate() {
		return template;
	}

	public void setTemplate(List<XPBlock> template) {
		this.template = template;
		ArrayList<String> store = new ArrayList<String>();
		for (XPBlock block : template) {
			store.add(block.getX() + " " + block.getY() + " " + block.getZ() + " " + block.getMaterial());
		}

		XPylons.getInstance().getConfig().set("template", store);
		XPylons.getInstance().saveConfig();
	}

	public void setActivationMaterial(Material activationMaterial) {
		this.activationMaterial = activationMaterial;
	}

	public void setPylonBox(AABox pylonBox) {
		this.pylonBox = pylonBox;
	}

	public void setMinPylonDistance(int minPylonDistance) {
		this.minPylonDistance = minPylonDistance;
	}

	public void setGrowthStopChance(float growthStopChance) {
		this.growthStopChance = growthStopChance;
	}

	public void setXpPerGrowth(double xpPerGrowth) {
		this.xpPerGrowth = xpPerGrowth;
	}

	public DBLayer getDBLayer() {
		return dblayer;
	}

}
