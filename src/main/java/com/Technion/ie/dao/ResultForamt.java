package com.Technion.ie.dao;

public class ResultForamt {
	
	private String subtree;
	private double prob;
	
	public ResultForamt (String subtree, double prob)
	{
		this.subtree = subtree;
		this.prob = prob;
	}
	
	public ResultForamt ()
	{
		this.subtree = "";
		this.prob = 0.0;
	}
	
	public void setSubtree (String newSubtree)
	{
		this.subtree = newSubtree;
	}
	
	public String getSubtree ()
	{
		return this.subtree;
	}

	public void setProb (double prob)
	{
		this.prob = prob;
	}
	
	public double getProb ()
	{
		return this.prob;
	}
}
