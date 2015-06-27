#coding:utf-8

require "pry"
require "json"
require_relative "./sentence_parser.rb"
#require_relative "./synonyms/levin.rb"


def distance(a, b) # Jaccar
	a_c = a.uniq.size
	b_c = b.uniq.size
	c = (a & b).size
	dist = 1 - c.to_f / (a_c + b_c - c)
	return dist
end

class Cluster

	def initialize(elemnts)
		if elemnts.class == Array
			@elemnts = elemnts
		else
			@elemnts = [elemnts]
		end
	end

	def get_tokens()
		return @elemnts.inject([]) do
			|mem, var|
			begin
				if var.class == Cluster
					mem += var.get_tokens();
				else
					mem += var["tokens"];
				end
			rescue
				binding.pry
			end 
		end.uniq
	end

	def get_elements()
		return @elemnts
	end

	def add_elments(elemnts)
		if elemnts.class == Array then
			@elemnts += elemnts
		else
			@elemnts.push elemnts
		end
	end

	def export()
		return @elemnts.map do |x|
			if x.class == Cluster
				next x.export
			else
				next x["title"]
			end
		end
	end
end

def nearest(a, clusters, used = [])
	nearest = clusters.each.with_index.min_by do 
		|x, idx|
		if a == x or used[idx]
			next 2
		else
			next distance(x.get_tokens, a.get_tokens)
		end 
	end

	if distance(nearest[0].get_tokens, a.get_tokens) == 0
		return nil
	else
		return nearest
	end
end


def nearest_dist(a, clusters)
	nearest = clusters.each.with_index.min_by do 
		|x, idx|
		if a == x
			next 2
		else
			next distance(x.get_tokens, a.get_tokens)
		end 
	end
	
	d = distance(nearest[0].get_tokens, a.get_tokens)
	return 2 if d == 0
	return d

end

def iteration(clusters)
	puts "--------------"
	clusters_new = []
	used = [false] * clusters.size
	clusters.each.with_index do |clust, index|
		if used[index] == false
			near = nearest(clust, clusters, used )
			if near != nil && used[near[1]] == false
				clusters_new.push(Cluster.new( [near[0], clust] ))
				p "#{near[1]} <---> #{index}"
				used[near[1]] = true
				used[index] = true
			else
				clusters_new.push(clust)
				p "self #{index}"
			end
		end
	end
	return clusters_new
end


class StopWords

	def initialize()
		parser = Parser.new
		@dict = {}
		File.new("stop-words_russian_2_ru.txt").readlines.each do |line|
			@dict[parser.convert(line.strip)[0]] = true;
		end
	end

	def check?(word)
		return @dict.has_key?(word)
	end

end


if __FILE__ == $0


	#fname = "captions.txt" #ARGV[0].gsub(/\.[^\.]+$/,"")
	fname = "gov_titles.txt" #ARGV[0].gsub(/\.[^\.]+$/,"")



	parser = Parser.new
	sw = StopWords.new

	datasets = []

	File.new(fname).readlines.each do |line|
		dataset = {}
		line.strip!
		t_data = parser.tokenize(line ,:default)
		dataset["title"] = line
		tokens = t_data.select{|x| x[1] == :word }.map{|x| x[0]}.map{|x|  parser.convert(x)[0] }.select{|x| not sw.check?(x)}
		dataset["tokens"] = tokens;
		datasets.push dataset
	end

	clusters = datasets.map{|x| Cluster.new( x ) }

	all = clusters.clone

	puts "start: #{clusters.size}"
	
	n = 0;
	not_uniq = clusters.select do |x| 
		n+=1;
		nearest_dist(x, clusters) < 1
		p n if n % 10 == 0
	end

	puts "not uniq: #{clusters.size}"
	#p nearest(clusters[1], clusters)



	while not_uniq.size != 1 do
		not_uniq = iteration(not_uniq)
	end


	File.write("clust.json" , not_uniq[0].export.to_json)


	#binding.pry


	#data = JSON.parse(File.read(ARGV[0]))
	#parser = Parser.new
	#t_data = parser.tokenize(ent["value"] ,:no_deffice)
	#lem = parser.convert(x[0])

	#v = VisializerHTML.new
	#v.test
end