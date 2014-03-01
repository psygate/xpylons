package com.psygate.xpylons.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.avaje.ebean.validation.NotNull;
import com.psygate.xpylons.geometry.AABox;
import com.psygate.xpylons.geometry.SimpleAABB;

@Entity
@Table(name = "xpylons_pylon")
public class XPylon implements SimpleAABB {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "block_id")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pylon")
	@NotNull
	private List<XPBlock> blocks = null;
	@Column(name = "lowx")
	private int lowX;
	@Column(name = "lowy")
	private int lowY;
	@Column(name = "lowz")
	private int lowZ;
	@Column(name = "highx")
	private int highX;
	@Column(name = "highy")
	private int highY;
	@Column(name = "highz")
	private int highZ;
	@Column(name = "creation_time")
	private long creation;
	@Column(name = "centerx")
	private int cx;
	@Column(name = "centery")
	private int cy;
	@Column(name = "centerz")
	private int cz;
	@Column(name = "worlduid")
	private String worldUID;
	@Column(name = "xp")
	private double xp;

	public XPylon() {

	}

	public XPylon(List<Block> addblocks, int cx, int cy, int cz, String worldUID, AABox box) {
		super();
		this.blocks = new LinkedList<XPBlock>();
		for (Block b : addblocks) {
			this.blocks.add(new XPBlock(b, this));
		}
		int wh = box.getWidth() / 2;
		int hh = box.getHeight() / 2;
		int dh = box.getDepth() / 2;
		this.lowX = cx - wh;
		this.lowY = cy - hh;
		this.lowZ = cz - dh;
		this.highX = cx + wh;
		this.highY = cy + hh;
		this.highZ = cz + dh;
		this.creation = System.currentTimeMillis();
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.worldUID = worldUID;
	}

	public XPylon(LinkedList<Block> addblocks, Block base, AABox box) {
		super();
		this.blocks = new LinkedList<XPBlock>();
		for (Block b : addblocks) {
			this.blocks.add(new XPBlock(b, this));
		}

		this.cx = base.getX();
		this.cy = base.getY();
		this.cz = base.getZ();
		this.worldUID = base.getWorld().getUID().toString();

		int wh = box.getWidth() / 2;
		int hh = box.getHeight() / 2;
		int dh = box.getDepth() / 2;
		this.lowX = cx - wh;
		this.lowY = cy - hh;
		this.lowZ = cz - dh;
		this.highX = cx + wh;
		this.highY = cy + hh;
		this.highZ = cz + dh;
		this.creation = System.currentTimeMillis();
	}

	public Block getCenterBlock() {
		return Bukkit.getWorld(UUID.fromString(getWorldUID())).getBlockAt(getCx(), getCy(), getCz());
	}

	public String getWorldUID() {
		return worldUID;
	}

	public void setWorldUID(String worldUID) {
		this.worldUID = worldUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<XPBlock> getBlocks() {
		return blocks;
	}

	public int getCx() {
		return cx;
	}

	public void setCx(int cx) {
		this.cx = cx;
	}

	public int getCy() {
		return cy;
	}

	public void setCy(int cy) {
		this.cy = cy;
	}

	public int getCz() {
		return cz;
	}

	public void setCz(int cz) {
		this.cz = cz;
	}

	public void setBlocks(List<XPBlock> blocks) {
		this.blocks = blocks;
	}

	public int getLowX() {
		return lowX;
	}

	public void setLowX(int lowX) {
		this.lowX = lowX;
	}

	public int getLowY() {
		return lowY;
	}

	public void setLowY(int lowY) {
		this.lowY = lowY;
	}

	public int getLowZ() {
		return lowZ;
	}

	public void setLowZ(int lowZ) {
		this.lowZ = lowZ;
	}

	public int getHighX() {
		return highX;
	}

	public void setHighX(int highX) {
		this.highX = highX;
	}

	public int getHighY() {
		return highY;
	}

	public void setHighY(int highY) {
		this.highY = highY;
	}

	public int getHighZ() {
		return highZ;
	}

	public void setHighZ(int highZ) {
		this.highZ = highZ;
	}

	public long getCreation() {
		return creation;
	}

	public void setCreation(long creation) {
		this.creation = creation;
	}

	public double getXp() {
		return xp;
	}

	public void setXp(double xp) {
		this.xp = xp;
	}

	@Override
	public int getX() {
		return getLowX();
	}

	@Override
	public int getY() {
		return getLowY();
	}

	@Override
	public int getZ() {
		return getLowZ();
	}

	@Override
	public int getWidth() {
		return getHighX() - getLowX();
	}

	@Override
	public int getHeight() {
		return getHighY() - getLowY();

	}

	@Override
	public int getDepth() {
		return getHighZ() - getLowZ();

	}

	@Override
	public int getMaxX() {
		return getHighX();
	}

	@Override
	public int getMaxY() {
		return getHighY();
	}

	@Override
	public int getMaxZ() {
		return getHighZ();
	}

	@Override
	public boolean intersects(SimpleAABB other) {
		return (getMaxX() >= other.getX() && getX() <= other.getMaxX())
				&& (getMaxY() >= other.getY() && getY() <= other.getMaxY())
				&& (getMaxZ() >= other.getZ() && getZ() <= other.getMaxZ());

	}

	@Override
	public boolean contains(int x, int y, int z) {
		return x <= getMaxX() && x >= getX() && y <= getMaxY() && y >= getY() && z <= getMaxZ() && z >= getZ();
	}
}
