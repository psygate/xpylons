package com.psygate.xpylons.entities;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "xpylons_block", uniqueConstraints = { @UniqueConstraint(columnNames = { "x", "y", "z" }) })
public class XPBlock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "x")
	private int x;
	@Column(name = "y")
	private int y;
	@Column(name = "z")
	private int z;
	@Column(name = "worlduid")
	@NotNull
	private String worlduid;
	@Column(name = "material")
	@NotNull
	private String material;
	@Column(name = "pylon_id")
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private XPylon pylon;

	public XPBlock() {

	}

	public XPBlock(int x, int y, int z, String worlduid, String material, XPylon parent) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.worlduid = worlduid;
		this.material = material;
		this.pylon = parent;
	}

	public XPBlock(Block b, XPylon pylon) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.worlduid = b.getWorld().getUID().toString();
		this.material = b.getType().name();
		this.pylon = pylon;
	}

	public XPBlock(int x, int y, int z, Material material) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.material = material.name();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getWorlduid() {
		return worlduid;
	}

	public void setWorlduid(String worlduid) {
		this.worlduid = worlduid;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public XPylon getPylon() {
		return pylon;
	}

	public void setPylon(XPylon pylon) {
		this.pylon = pylon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Block toBlock() {
		return Bukkit.getServer().getWorld(UUID.fromString(worlduid)).getBlockAt(x, y, z);
	}
}
